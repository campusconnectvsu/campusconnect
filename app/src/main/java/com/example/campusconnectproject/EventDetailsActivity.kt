package com.example.campusconnectproject

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.databinding.ActivityEventDetailsBinding

class EventDetailsActivity : AppCompatActivity() {

    // view binding event details layout
    private lateinit var binding: ActivityEventDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // handle back btn clicks
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        // go back to previous screen
        binding.returnBackBtn.setNavigationOnClickListener {
            finish()
        }

        // Retrieve Event Data from Intent
        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_EVENT", Event::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_EVENT")
        }

        // Fill UI with actual Event data
        event?.let { currentEvent ->
            binding.tvTitle.text = currentEvent.title
            binding.ivEventHeader.setImageResource(currentEvent.imageResId)
            
            binding.tvDate.text = currentEvent.date.replace("\n", " ")
            binding.tvTime.text = "Event Time: Scheduled" 
            
            binding.tvLocationTitle.text = currentEvent.location
            binding.tvLocationAddress.text = "Campus Grounds" 
            
            binding.tvOrganizer.text = currentEvent.organizer
            binding.tvDescription.text = currentEvent.description

            // update btn based on join status
            updateButtonStates(currentEvent)

            // send a notification when joining  an event
            binding.btnAccept.setOnClickListener {
                EventManager.joinEvent(currentEvent)
                Toast.makeText(this, "Joined: ${currentEvent.title}", Toast.LENGTH_SHORT).show()
                updateButtonStates(currentEvent)

                // send notification to user
                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener
                NotificationsActivity.sendNotification(
                    db = db,
                    targetUid = uid,
                    title = "Joined event: ${currentEvent.title}",
                    body =  "${currentEvent.date} - ${currentEvent.location}",
                    type = "event"
                )
            }

            // leave the event
            binding.btnReject.setOnClickListener {
                EventManager.leaveEvent(currentEvent)
                Toast.makeText(this, "Removed: ${currentEvent.title}", Toast.LENGTH_SHORT).show()
                updateButtonStates(currentEvent)
            }
        } ?: run {
            // Handle if event is null
            binding.tvTitle.text = "Event Details"
        }
    }

    // update btn states based on join status
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
