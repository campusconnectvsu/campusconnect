package com.example.campusconnectproject

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.databinding.ActivityEventDetailsBinding

class EventDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        binding.tbar.setNavigationOnClickListener {
            finish()
        }

        // --- Retrieve Event Data Safely --- //
        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_EVENT", Event::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_EVENT")
        }

        // --- Fill UI with actual Event data --- //
        event?.let { currentEvent ->
            binding.tvTitle.text = currentEvent.title
            binding.ivEventHeader.setImageResource(currentEvent.imageResId)
            
            binding.tvDate.text = currentEvent.date.replace("\n", " ")
            binding.tvTime.text = "Event Time: Scheduled" 
            
            binding.tvLocationTitle.text = currentEvent.location
            binding.tvLocationAddress.text = "Campus Grounds" 
            
            binding.tvOrganizer.text = currentEvent.organizer
            binding.tvDescription.text = currentEvent.description

            updateButtonStates(currentEvent)

            binding.btnAccept.setOnClickListener {
                EventManager.joinEvent(currentEvent)
                Toast.makeText(this, "Joined: ${currentEvent.title}", Toast.LENGTH_SHORT).show()
                updateButtonStates(currentEvent)
            }

            binding.btnReject.setOnClickListener {
                EventManager.leaveEvent(currentEvent)
                Toast.makeText(this, "Removed: ${currentEvent.title}", Toast.LENGTH_SHORT).show()
                updateButtonStates(currentEvent)
            }
        } ?: run {
            binding.tvTitle.text = "Event Details"
        }
    }

    private fun updateButtonStates(event: Event) {
        if (EventManager.isJoined(event)) {
            binding.btnAccept.text = "Joined"
            binding.btnAccept.isEnabled = false
            binding.btnReject.visibility = android.view.View.VISIBLE
            binding.btnReject.text = "Leave Event"
        } else {
            binding.btnAccept.text = "Join Event"
            binding.btnAccept.isEnabled = true
            binding.btnReject.visibility = android.view.View.GONE
        }
    }
}
