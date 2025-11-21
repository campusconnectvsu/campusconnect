package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val notificationBell: ImageView = findViewById(R.id.notification_bell)
        notificationBell.setOnClickListener {
            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        // Setup Events RecyclerView
        val eventsRecyclerView: RecyclerView = findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val events = listOf(
            Event("International Band Mu...", "Foster Hall, Basement", "10\nJUNE", R.drawable.ic_launcher_background),
            Event("Jo Malone", "Radius Gallery", "10\nJUNE", R.drawable.ic_launcher_background)
        )

        val adapter = EventAdapter(events)
        eventsRecyclerView.adapter = adapter
    }
}
