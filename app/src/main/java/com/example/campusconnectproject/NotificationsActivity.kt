package com.example.campusconnectproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.ActivityNotificationsBinding

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.backBtn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.backBtn.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        // Mock data for now - in a real app, this would come from a local database or Firestore
        val mockNotifications = listOf(
            NotificationModel(
                "1",
                "New Event: Tech Talk",
                "A new tech talk has been scheduled in Foster Hall.",
                System.currentTimeMillis() - 3600000,
                "event",
                false
            ),
            NotificationModel(
                "2",
                "Message from Sarah",
                "Hey! Are you going to the concert tonight?",
                System.currentTimeMillis() - 7200000,
                "message",
                false
            ),
            NotificationModel(
                "3",
                "System Update",
                "Your profile was successfully updated.",
                System.currentTimeMillis() - 86400000,
                "general",
                true
            )
        )

        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notificationsRecyclerView.adapter = NotificationAdapter(mockNotifications)
    }
}
