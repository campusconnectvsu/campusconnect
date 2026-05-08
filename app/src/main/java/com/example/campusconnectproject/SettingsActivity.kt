package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.example.campusconnectproject.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    // Binding for the setting activity layout
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set up system bar insets to prevent content overlapping
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // go back button
        binding.returnBackBtn.setNavigationOnClickListener {
            finish()
        }

        // go to changed password screen
        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        // go to privacy settings screen
        binding.btnPrivacySettings.setOnClickListener {
            val intent = Intent(this, PrivacySettingsActivity::class.java)
            startActivity(intent)
        }

        // switch for dark mode
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binding.switchDarkMode.isChecked = isDarkMode

        // switch to dark mode listener
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        // placeholder for help center
        binding.btnHelpCenter.setOnClickListener {
            Toast.makeText(this, "Opening Help Center...", Toast.LENGTH_SHORT).show()
        }

        // place holder for bug report
        binding.btnReportIssue.setOnClickListener {
            Toast.makeText(this, "Report an Issue screen coming soon!", Toast.LENGTH_SHORT).show()
        }

        // show confirmation dialog before deleting account
        binding.btnDeleteAccount.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete") { _, _ ->
                    Toast.makeText(this, "Account Deleted", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

        // logout button
        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, com.example.campusconnectproject.loginsignup.LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }
}
