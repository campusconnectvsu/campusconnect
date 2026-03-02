package com.example.campusconnectproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Get views
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvDateTime: TextView = findViewById(R.id.tvDateTime)
        val tvLocation: TextView = findViewById(R.id.tvLocation)
        val tvOrganizer: TextView = findViewById(R.id.tvOrganizer)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val btnAccept: Button = findViewById(R.id.btnAccept)
        val btnReject: Button = findViewById(R.id.btnReject)

        // Initial data (consider passing these as intent extras later)
        tvTitle.text = "International Band Music Concert"
        tvDateTime.text = "14 December, 2021 • 4:00PM - 9:00PM"
        tvLocation.text = "Daniel Gymnasium, 4th Ave, Petersburg, VA 23806"
        tvOrganizer.text = "Organizer: Ashfaq Sayem"
        tvDescription.text = "Enjoy your favorite music and have a great time with friends and family."

        // Simple button actions
        btnAccept.setOnClickListener {
            Toast.makeText(this, "You accepted the event", Toast.LENGTH_SHORT).show()
        }

        btnReject.setOnClickListener {
            Toast.makeText(this, "You rejected the event", Toast.LENGTH_SHORT).show()
        }
    }
}
