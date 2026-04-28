package com.example.campusconnectproject.loginsignup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val name = binding.signupFullname.text.toString().trim()
            val email = binding.signupEmail.text.toString().trim()
            val password = binding.signupPassword.text.toString().trim()
            val confirmPassword = binding.signupConfirm.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, " Fields can't be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.signupButton.isEnabled = false

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val uid = authResult.user?.uid
                    if (uid == null) {
                        Log.e("SIGNUP", "UID is null")
                        Toast.makeText(this, "Signup error", Toast.LENGTH_SHORT).show()
                        binding.signupButton.isEnabled = true
                        return@addOnSuccessListener
                    }
                    Log.d("SINGUP", "Auth success uid=$uid, writing to firestore...")
                    val u_Map = hashMapOf(
                        "uid" to uid,
                        "name" to name.lowercase(),
                        "email" to email
                    )

                    db.collection("users").document(uid)
                        .set(u_Map)
                        .addOnSuccessListener {
                            Log.d("SIGNUP", "Firestore write SUCCESS")
                            Toast.makeText(this, "Signup successfully", Toast.LENGTH_SHORT).show()


                            firebaseAuth.signOut()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Log.e("SIGNUP", "Firestore write FAILED: ${e.message}", e)
                            Toast.makeText(
                                this, "Failed to save profile: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()

                            binding.signupButton.isEnabled = true
                        }

                }
                .addOnFailureListener { e ->
                    Log.e("SIGNUP", "Auth FAILED: ${e.message}")
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    binding.signupButton.isEnabled = true

                }
        }
        binding.LoginRedirectText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}