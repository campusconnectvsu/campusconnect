package com.example.campusconnectproject

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.databinding.ActivityEditEventBinding

class EditEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // --- View Binding Setup --- //
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Modern Back Handling --- //
        onBackPressedDispatcher.addCallback(this) {
            // Simply finish the activity to return to the previous screen
            finish()
        }

        // Get the data from the intent
        val title = intent.getStringExtra("EVENT_TITLE")
        val location = intent.getStringExtra("EVENT_LOCATION")
        val date = intent.getStringExtra("EVENT_DATE")

        // Set the text in the EditText fields using binding
        binding.editEventTitle.setText(title)
        binding.editEventLocation.setText(location)
        binding.editEventDate.setText(date)

        // Find the save button and set a click listener
        binding.saveEventButton.setOnClickListener {
            // Modern Alternative to FLAG_ACTIVITY_CLEAR_TOP for simple "Save and Return":
            // Just finish() to return to the existing HomeActivity instance.
            // If data needs to be passed back, consider using setResult() here.
            finish()
        }
    }
}
