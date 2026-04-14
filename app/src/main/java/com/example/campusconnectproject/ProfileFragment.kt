package com.example.campusconnectproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.campusconnectproject.databinding.FragmentProfileBinding
import com.example.campusconnectproject.loginsignup.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    // ActivityResultLauncher for the Photo Picker
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the photo picker.
        if (uri != null) {
            // Update the profile image with the selected URI
            binding.profileImage.setImageURI(uri)
            // Remove padding if it was used for the placeholder icon
            binding.profileImage.setPadding(0, 0, 0, 0)
            Toast.makeText(requireContext(), "Profile picture updated!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
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

        firebaseAuth = FirebaseAuth.getInstance()

        // Set the user's email
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            binding.userEmailTextView.text = currentUser.email
        } else {
            // If no user is signed in, redirect to login
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }

        // Handle Change Profile Picture click
        binding.fabChangeProfilePicture.setOnClickListener {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Handle Edit Profile button click
        binding.cardEditProfile.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Handle Settings button click
        binding.cardSettings.setOnClickListener {
            val intent = Intent(requireActivity(), SettingsActivity::class.java)
            startActivity(intent)
        }

        // Handle Notifications click (placeholder)
        binding.cardNotifications.setOnClickListener {
            Toast.makeText(context, "Notification settings coming soon", Toast.LENGTH_SHORT).show()
        }

        // Handle Interests click (placeholder)
        binding.cardInterests.setOnClickListener {
            Toast.makeText(context, "Interest selection coming soon", Toast.LENGTH_SHORT).show()
        }

        // Handle logout button click
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            // Redirect to the Login screen
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
