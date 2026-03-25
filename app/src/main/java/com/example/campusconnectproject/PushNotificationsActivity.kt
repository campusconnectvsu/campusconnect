package com.example.campusconnectproject


import android.content.Context
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PushNotificationsActivity : AppCompatActivity() {

    private lateinit var messages: Switch
    private lateinit var events: Switch
    private lateinit var reminders: Switch
    private lateinit var system: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_notfication)

        // Get SharedPreferences
        val prefs = getSharedPreferences("push_settings", Context.MODE_PRIVATE)

        messages = findViewById(R.id.swiMessage)
        events = findViewById(R.id.swEvent)
        reminders = findViewById(R.id.swiReminders)
        system = findViewById(R.id.switchSystem)

        // Load saved states
        messages.isChecked = prefs.getBoolean("messages", true)
        events.isChecked = prefs.getBoolean("events", true)
        reminders.isChecked = prefs.getBoolean("reminders", true)
        system.isChecked = prefs.getBoolean("system", true)

        // Listen for changes
        messages.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("messages", isChecked)
        }

        events.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("events", isChecked)
        }

        reminders.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("reminders", isChecked)
        }

        system.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("system", isChecked)
        }
    }

    private fun saveSetting(key: String, value: Boolean) {
        val prefs = getSharedPreferences("push_settings", Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
    }
}
