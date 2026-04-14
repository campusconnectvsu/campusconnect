package com.example.campusconnectproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Handle Change Photo button
        binding.fabChangePhoto.setOnClickListener {
            Toast.makeText(this, "Gallery access coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Handle Save Profile button
        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etFullName.text.toString()
            val bio = binding.etBio.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // In a real app, you'd save this to Firebase Firestore or Realtime Database here
            Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
            
            // Return to Profile screen
            finish()
        }
    }
}
