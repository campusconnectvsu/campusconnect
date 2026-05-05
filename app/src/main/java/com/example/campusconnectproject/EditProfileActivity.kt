package com.example.campusconnectproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val dat_sto = FirebaseFirestore.getInstance()
    private val fireauth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        binding.returnBackBtn.setNavigationOnClickListener {
            finish()
        }


        val currentUser = fireauth.currentUser
        if (currentUser == null){
            finish()
            return
        }


        val uid = currentUser.uid
        val email = currentUser.email?: ""


        dat_sto.collection("users").document(uid).get()
            .addOnSuccessListener { doc->
                binding.etFullName.setText(doc.getString("name")?:"")
                binding.etBio.setText(doc.getString("bio")?:"")
            }

        // Handle Change Photo button
        binding.fabChangePhoto.setOnClickListener {
            Toast.makeText(this, "Gallery access coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Handle Save Profile button
        binding.btnSaveProfile.setOnClickListener {
            android.util.Log.d("SAVE_TEST", "Save button clicked, uid=$uid")
            val uname = binding.etFullName.text.toString()
            val bio = binding.etBio.text.toString()

            if (uname.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnSaveProfile.isEnabled = false
            binding.btnSaveProfile.text = "Saving"


            val userMap = hashMapOf(
                "uid" to uid,
                "email" to email,
                "name" to uname.lowercase(),
                "bio" to bio

            )



            dat_sto.collection("users").document(uid)
                .set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show()
                    finish()

                }
                .addOnFailureListener { error->
                    binding.btnSaveProfile.isEnabled = true
                    binding.btnSaveProfile.text = "Save Profile"
                    Toast.makeText(this, "Failed: ${error.message}", Toast.LENGTH_LONG).show()
                }

        }
    }
}
