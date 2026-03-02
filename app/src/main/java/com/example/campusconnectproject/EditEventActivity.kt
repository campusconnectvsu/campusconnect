package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        // Find the EditText fields
        val titleEditText: EditText = findViewById(R.id.editEventTitle)
        val locationEditText: EditText = findViewById(R.id.editEventLocation)
        val dateEditText: EditText = findViewById(R.id.editEventDate)

        // Get the data from the intent
        val title = intent.getStringExtra("EVENT_TITLE")
        val location = intent.getStringExtra("EVENT_LOCATION")
        val date = intent.getStringExtra("EVENT_DATE")

        // Set the text in the EditText fields
        titleEditText.setText(title)
        locationEditText.setText(location)
        dateEditText.setText(date)

        // Find the save button and set a click listener
        val saveButton: Button = findViewById(R.id.saveEventButton)
        saveButton.setOnClickListener {
            // Create an intent to go back to the HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            // Add flags to clear the back stack and bring the existing HomeActivity to the front
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Finish the current activity
        }
    }
}
