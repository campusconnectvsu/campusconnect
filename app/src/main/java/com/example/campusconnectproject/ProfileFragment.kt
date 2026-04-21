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


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val fire_Auth = FirebaseAuth.getInstance()
    private val fire_store = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private lateinit var postsAdapter: PostsAdapter
    private var cameraImageUri: Uri? = null


    private val pickProfilePic = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.profImage.setImageURI(uri)
            Toast.makeText(requireContext(), "Profile Pic updated", Toast.LENGTH_SHORT).show()
        }
    }


    private val pickPostImage = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) uploadPost(uri)
    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { uploadPost(it) }
    }


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


        // Set the user's email
        val currentUser = fire_Auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            return
        }

        postsAdapter = PostsAdapter(emptyList())
        binding.pGridReView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.pGridReView.adapter = postsAdapter
        setupButtons()


    }

    override fun onResume() {
        super.onResume()
        load_UData()
        loadPosts()
    }


    private fun load_UData() {
        val currentUser = fire_Auth.currentUser ?: return
        binding.userEmailTV.text = currentUser.email

        fire_store.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name") ?: currentUser.email ?: "user"
                val bio = doc.getString("bio") ?: ""
                binding.tUserName.text = name
                binding.tvTbarUname.text = name
                if ( bio.isNotEmpty()){
                    binding.tBio.text = bio
                    binding.tBio.visibility = View.VISIBLE
                } else {
                    binding.tBio.visibility = View.GONE
                }
            }

        fire_store.collection("users").document(currentUser.uid).collection("followers").get()
            .addOnSuccessListener { binding.tvFollowerC.text = it.size().toString() }
        fire_store.collection("users").document(currentUser.uid).collection("following").get()
            .addOnSuccessListener { binding.tFollowingCount.text = it.size().toString() }

    }

    private fun setupButtons() {
        binding.fabChangeProfilePicture.setOnClickListener {
            pickProfilePic.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.cardEditProf.setOnClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        binding.ShareBn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Check ut my campus connect profile")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Profile"))
        }


        binding.btnNotifi.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationsActivity::class.java))
        }

        binding.btnMenu.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingsActivity:: class.java))
        }

        binding.fabNewPost.setOnClickListener {
            showAddPostBottomSheet()
        }

        binding.drawEditProf.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        binding.changePsWordDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }

        binding.draPrivacy.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), PrivacySettingsActivity::class.java))
        }

        binding.DeleteAccount.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), DeleteAccountActivity::class.java))
        }

        binding.PushNotifiDrawer.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), PushNotificationsActivity::class.java))
        }

        binding.drGMode.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            startActivity(Intent(requireActivity(), GhostModeActivity::class.java))
        }

        binding.drLogout.setOnClickListener {
            binding.drawerLayout.closeDrawers()
            fire_Auth.signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


    private fun showAddPostBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val sheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_sheet_add_post, null)


        sheetView.findViewById<View>(R.id.op_Cam).setOnClickListener {
            dialog.dismiss()
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) launchCamera()
            else req_Cam_Permission.launch(Manifest.permission.CAMERA)
        }

        sheetView.findViewById<View>(R.id.op_photos).setOnClickListener {
            dialog.dismiss()
            pickPostImage.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        dialog.setContentView(sheetView)
        dialog.show()
    }

    private fun launchCamera() {
        val values = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "campus_post${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        cameraImageUri = uri
        if (uri != null) takePhoto.launch(uri)
        else Toast.makeText(requireContext(), "Could not open camera", Toast.LENGTH_SHORT).show()
    }

    private fun uploadPost(uri: Uri) {
        val uid = fire_Auth.currentUser?.uid ?: return
        Toast.makeText(requireContext(), "Uploading...", Toast.LENGTH_SHORT).show()

        val ref = storage.reference.child("posts/$uid/${System.currentTimeMillis()}.jpg")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { url ->
                    fire_store.collection("posts").add(
                        hashMapOf("userId" to uid, "imageUrl" to url.toString())
                    ).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Posted", Toast.LENGTH_SHORT).show()
                        loadPosts()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("PROFILE", "Upload failed: ${e.message}")
                Toast.makeText(requireContext(), "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
            }


    }

    private fun loadPosts() {
        val uid = fire_Auth.currentUser?.uid ?: return
        fire_store.collection("posts").whereEqualTo("userId", uid).get()
            .addOnSuccessListener { result ->
                val urls = result.mapNotNull { it.getString("imageUrl") }
                postsAdapter.update_Data(urls)
                binding.tvPostCount.text = urls.size.toString()
                if (urls.isEmpty()) {
                    binding.PostsContainer.visibility = View.VISIBLE
                    binding.pGridReView.visibility = View.GONE
                } else {
                    binding.PostsContainer.visibility = View.GONE
                    binding.pGridReView.visibility = View.VISIBLE
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
