package com.example.campusconnectproject


import android.content.Context
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GhostModeActivity : AppCompatActivity() {

    private lateinit var swGhMode: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ghost_mode)

        val prefs = getSharedPreferences("privacy_settings", Context.MODE_PRIVATE)

        swGhMode = findViewById(R.id.swGMode)

        // Load saved value
        swGhMode.isChecked = prefs.getBoolean("ghost_mode", false)

        // Listener
        swGhMode.setOnCheckedChangeListener { _, isOn ->
            saveSetting("ghost_mode", isOn)
        }
    }

    private fun saveSetting(key: String, value: Boolean) {
        val prefs = getSharedPreferences("privacy_settings", Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
    }
}
