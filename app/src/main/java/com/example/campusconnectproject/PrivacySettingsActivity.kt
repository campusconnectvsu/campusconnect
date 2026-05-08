package com.example.campusconnectproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campusconnectproject.databinding.ActivityPrivacySettingsBinding

class PrivacySettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable Edge-to-Edge display
        enableEdgeToEdge()
        
        binding = ActivityPrivacySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Window Insets for top padding (status bar/camera cutout)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the toolbar back navigation
        binding.returnBackBtn.setNavigationOnClickListener {
            finish()
        }

        //Switch Listeners
        binding.switchPrivateProfile.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Profile is now Private" else "Profile is now Public"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        binding.switchActivityStatus.setOnCheckedChangeListener { _, isChecked ->
            val message = if (isChecked) "Activity status shown" else "Activity status hidden"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // Button Listeners
        binding.btnBlockedAccounts.setOnClickListener {
            Toast.makeText(this, "Opening Blocked Accounts list...", Toast.LENGTH_SHORT).show()
        }

        binding.btnDownloadData.setOnClickListener {
            Toast.makeText(this, "Preparing your data for download...", Toast.LENGTH_SHORT).show()
        }
    }
}
