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
import com.example.campusconnectproject.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Enable Edge-to-Edge display
        enableEdgeToEdge()
        
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Handle Window Insets to avoid overlapping with System Bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the toolbar back navigation
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // --- Change Password Navigation --- //
        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        // --- Privacy Settings Navigation --- //
        binding.btnPrivacySettings.setOnClickListener {
            val intent = Intent(this, PrivacySettingsActivity::class.java)
            startActivity(intent)
        }

        // --- Dark Mode Logic --- //
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binding.switchDarkMode.isChecked = isDarkMode

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // --- Support & Help Logic --- //
        binding.btnHelpCenter.setOnClickListener {
            Toast.makeText(this, "Opening Help Center...", Toast.LENGTH_SHORT).show()
        }

        binding.btnReportIssue.setOnClickListener {
            Toast.makeText(this, "Report an Issue screen coming soon!", Toast.LENGTH_SHORT).show()
        }

        // --- Delete Account Logic --- //
        binding.btnDeleteAccount.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Delete") { _, _ ->
                    Toast.makeText(this, "Account Deleted", Toast.LENGTH_LONG).show()
                    // Add actual deletion logic here
                }
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }
}
