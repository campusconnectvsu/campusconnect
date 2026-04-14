package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnectproject.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    // --- Data Lists and Adapters --- //
    private val upcomingEvents = mutableListOf<Event>()
    private val currentEvents = mutableListOf<Event>()
    private val pastEvents = mutableListOf<Event>()

    private lateinit var upcomingEventsAdapter: EventAdapter
    private lateinit var currentEventsAdapter: EventAdapter
    private lateinit var pastEventsAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable Edge-to-Edge
        enableEdgeToEdge()
        
        // --- View Binding Setup --- //
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // padding top for status bar, padding bottom for navigation bar
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Modern Back Handling --- //
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        // --- Navigation to Notifications --- //
        binding.notificationBell.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        // --- Setup All RecyclerViews --- //
        setupRecyclerView(binding.upcomingEventsRecyclerView, upcomingEvents, true)
        setupRecyclerView(binding.currentEventsRecyclerView, currentEvents, false)
        setupRecyclerView(binding.pastEventsRecyclerView, pastEvents, false)
        
        // --- Add Event Functionality --- //
        binding.fab.setOnClickListener {
            val newEvent = Event("New Campus Event", "Location to be decided", "20\nJUNE", R.drawable.vsu_logo)
            upcomingEvents.add(newEvent)
            upcomingEventsAdapter.notifyItemInserted(upcomingEvents.size - 1)
            binding.upcomingEventsRecyclerView.scrollToPosition(upcomingEvents.size - 1)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, events: MutableList<Event>, isUpcoming: Boolean) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        
        // Create and set the adapter
        val adapter = EventAdapter(events)
        recyclerView.adapter = adapter
        
        if (isUpcoming) {
            upcomingEventsAdapter = adapter
            events.addAll(listOf(
                Event("International Band Mu...", "Foster Hall, Basement", "10\nJUNE", R.drawable.vsu_logo),
                Event("Jo Malone", "Radius Gallery", "10\nJUNE", R.drawable.vsu_logo),
                Event("Art Club Mixer", "Singleton Center", "12\nJUNE", R.drawable.vsu_logo),
                Event("Study Group Session", "Johnston Memorial Library", "14\nJUNE", R.drawable.vsu_logo),
                Event("Outdoor Movie Night", "The VSU Quad", "15\nJUNE", R.drawable.vsu_logo)
            ))
        } else {
            // Add filler data for current and past events for now
             events.addAll(listOf(
                Event("Sample Event 1", "Sample Location", "1\nJAN", R.drawable.vsu_logo),
                Event("Sample Event 2", "Sample Location", "2\nFEB", R.drawable.vsu_logo)
            ))
        }

        // In a production app, use DiffUtil here instead of notifyDataSetChanged()
        adapter.notifyDataSetChanged()
    }
}
