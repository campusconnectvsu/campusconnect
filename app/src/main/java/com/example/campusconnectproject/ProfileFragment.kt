package com.example.campusconnectproject

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.campusconnectproject.databinding.FragmentProfileBinding
import com.example.campusconnectproject.loginsignup.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide


class ProfileFragment : Fragment() {

    // view binding for profile layout
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    // firebase db , auth and storage  instances
    private val fire_Auth = FirebaseAuth.getInstance()
    private val fire_store = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // adapter for posts
    private lateinit var postsAdapter: PostsAdapter
    // launch camera to take photo
    private var cameraImageUri: Uri? = null


    // launch gallery to pick profile pics
    private val pickProfilePic = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            upload_ProfPic(uri)
        }
    }


    // launch gallery to post images
    private val pickPostImage = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) uploadPost(uri)
    }

    // if photo is taken successfully, upload it
    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { uploadPost(it) }
    }


    // request camera permission
    private val req_Cam_Permission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { grand ->
        if (grand) launchCamera()
        else Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // redirect to login if not logged in
        val currentUser = fire_Auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            return
        }

        // set up recycler view for posts
        postsAdapter = PostsAdapter(emptyList())
        binding.pGridReView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.pGridReView.adapter = postsAdapter
        setupButtons()


    }

    // reload data when going back
    override fun onResume() {
        super.onResume()
        load_UData()
        loadPosts()
    }


    //load user data from firestore
    private fun load_UData() {
        // get current user
        val currentUser = fire_Auth.currentUser ?: return
        binding.userEmailTV.text = currentUser.email

        fire_store.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name") ?: currentUser.email ?: "user"
                val bio = doc.getString("bio") ?: ""
                val prof_picUrl = doc.getString("profilePicUrl")
                // update UI
                binding.tUserName.text = name
                binding.tvTbarUname.text = name
                // show bio if not empty
                if (bio.isNotEmpty()) {
                    binding.tBio.text = bio
                    binding.tBio.visibility = View.VISIBLE
                } else {
                    binding.tBio.visibility = View.GONE
                }
                // show profile pic if not empty
                if (!prof_picUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(prof_picUrl)
                        .circleCrop()
                        .placeholder(com.example.campusconnectproject.R.drawable.ic_person)
                        .into(binding.profImage)
                }
            }


        // shows follower count
        fire_store.collection("users").document(currentUser.uid).collection("followers").get()
            .addOnSuccessListener { binding.tvFollowerC.text = it.size().toString() }
        // shows following count
        fire_store.collection("users").document(currentUser.uid).collection("following").get()
            .addOnSuccessListener { binding.tFollowingCount.text = it.size().toString() }

    }

    // Upload and save profile picture to firebase storage
    private  fun upload_ProfPic(uri: Uri){
        // get current user UID
        val uid = fire_Auth.currentUser?.uid?: return
        Toast.makeText(requireContext(), "Uploading profile picture...", Toast.LENGTH_SHORT).show()

        // upload image to firebase storage
        val storage_ref = storage.reference.child("profile_pics/$uid.jpg")
        storage_ref.putFile(uri)
            .addOnSuccessListener {
                storage_ref.downloadUrl.addOnSuccessListener { downloadUrl->
                    // save download url to firestore
                    fire_store.collection("users").document(uid)
                        .update("profilePicUrl", downloadUrl.toString())
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "profile picture updated!", Toast.LENGTH_SHORT).show()
                            Glide.with(this)
                                .load(downloadUrl)
                                .circleCrop()
                                .into(binding.profImage)
                        }
                }
            }
            .addOnFailureListener { e->
                Toast.makeText(requireContext(), "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }




    // set up buttons
    private fun setupButtons() {
        // change profile picture
        binding.fabChangeProfilePicture.setOnClickListener {
            pickProfilePic.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        // go to edit profile screen
        binding.cardEditProf.setOnClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        // share profile
        binding.ShareBn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Check out my campus connect profile")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Profile"))
        }

        // go to notifications screen
        binding.btnNotifi.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationsActivity::class.java))
        }

        // go to settings screen
        binding.btnMenu.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingsActivity::class.java))
        }

        // open bottom sheet to add post
        binding.fabNewPost.setOnClickListener {
            showAddPostBottomSheet()
        }

        // drawer buttons to edit profile
        binding.drawEditProf.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        // drawer buttons to change password
        binding.changePsWordDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }

        // drawer buttons to change privacy settings
        binding.draPrivacy.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), PrivacySettingsActivity::class.java))
        }

        // drawer buttons to delete account
        binding.DeleteAccount.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), DeleteAccountActivity::class.java))
        }

        // drawer buttons to push notifications
        binding.PushNotifiDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), PushNotificationsActivity::class.java))
        }

        // drawer buttons to ghost mode
        binding.drGMode.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), GhostModeActivity::class.java))
        }

        // drawer buttons to logout
        binding.drLogout.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            fire_Auth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


    // show bottom sheet with options to add post or pick from gallery
    private fun showAddPostBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val sheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_sheet_add_post, null)


        // request and launch camera if granted
        sheetView.findViewById<View>(R.id.op_Cam).setOnClickListener {
            dialog.dismiss()
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) launchCamera()
            else req_Cam_Permission.launch(Manifest.permission.CAMERA)
        }

        // open gallery to pick image
        sheetView.findViewById<View>(R.id.op_photos).setOnClickListener {
            dialog.dismiss()
            pickPostImage.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        dialog.setContentView(sheetView)
        dialog.show()
    }

    // creates a new intent to launch camera
    private fun launchCamera() {
        // values for a  new image file
        val values = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "campus_post${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        // Insert the entry into the MediaStore
        val uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        cameraImageUri = uri
        if (uri != null) takePhoto.launch(uri)
        else Toast.makeText(requireContext(), "Could not open camera", Toast.LENGTH_SHORT).show()
    }

    // upload post to firestore
    private fun uploadPost(uri: Uri) {
        val uid = fire_Auth.currentUser?.uid ?: return
        Toast.makeText(requireContext(), "Uploading...", Toast.LENGTH_SHORT).show()

        // upload to firebase storage under posts/$uid
        val ref = storage.reference.child("posts/$uid/${System.currentTimeMillis()}.jpg")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->
                    // save download url to firestore
                    fire_store.collection("posts").add(
                        hashMapOf("userId" to uid, "imageUrl" to url.toString(), "timestamp" to System.currentTimeMillis())
                    ).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Posted", Toast.LENGTH_SHORT).show()
                        loadPosts()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("PROFILE", "Upload failed: ${e.message}")
                Toast.makeText(requireContext(), "Upload failed: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }


    }

    // load all posts for current user
    private fun loadPosts() {
        // get current user UID
        val uid = fire_Auth.currentUser?.uid ?: return
        fire_store.collection("posts").whereEqualTo("userId", uid).get()
            .addOnSuccessListener { result ->
                val urls = result.mapNotNull { it.getString("imageUrl") }
                postsAdapter.update_Data(urls)
                binding.tvPostCount.text = urls.size.toString()
                // show empty text if no posts
                if (urls.isEmpty()) {
                    binding.PostsContainer.visibility = View.VISIBLE
                    binding.pGridReView.visibility = View.GONE
                } else {
                    binding.PostsContainer.visibility = View.GONE
                    binding.pGridReView.visibility = View.VISIBLE
                }
            }
    }

    // nullify binding to prevent memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
