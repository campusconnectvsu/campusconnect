package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.loginsignup.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // Check if user is already signed in
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                // User is signed in, go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // No user is signed in, go to LoginActivity
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000) // 2 second delay
    }
}
