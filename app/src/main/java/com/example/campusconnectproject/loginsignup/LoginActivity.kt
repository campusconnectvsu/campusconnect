package com.example.campusconnectproject.loginsignup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.MainActivity
import com.example.campusconnectproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    // binding for login xml
    private lateinit var binding: ActivityLoginBinding
    // Firebase authentication instance
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get auth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // handle login btn click
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            // check if password is empty or not
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // clear previously joined events
                            com.example.campusconnectproject.EventManager.clearAll()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // takes you back to the sign up page
        binding.SignupRedirectText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            //finish()
        }
    }
}