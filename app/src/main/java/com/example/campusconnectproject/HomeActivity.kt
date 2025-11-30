package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    // --- Data Lists and Adapters --- //
    private val upcomingEvents = mutableListOf<Event>()
    private val currentEvents = mutableListOf<Event>()
    private val pastEvents = mutableListOf<Event>()

    private lateinit var upcomingEventsAdapter: EventAdapter
    private lateinit var currentEventsAdapter: EventAdapter
    private lateinit var pastEventsAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // --- Navigation to Messages --- //
        val notificationBell: ImageView = findViewById(R.id.notification_bell)
        notificationBell.setOnClickListener {
            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        // --- Setup All RecyclerViews --- //
        setupRecyclerView(R.id.upcomingEventsRecyclerView, upcomingEvents, true)
        setupRecyclerView(R.id.currentEventsRecyclerView, currentEvents, false)
        setupRecyclerView(R.id.pastEventsRecyclerView, pastEvents, false)
        
        // --- Add Event Functionality --- //
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val newEvent = Event("New Campus Event", "Location to be decided", "20\nJUNE", R.drawable.ic_launcher_background)
            upcomingEvents.add(newEvent)
            upcomingEventsAdapter.notifyItemInserted(upcomingEvents.size - 1)
            findViewById<RecyclerView>(R.id.upcomingEventsRecyclerView).scrollToPosition(upcomingEvents.size - 1)
        }
    }

    private fun setupRecyclerView(recyclerViewId: Int, events: MutableList<Event>, isUpcoming: Boolean) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        
        // Create and set the adapter
        val adapter = EventAdapter(events)
        recyclerView.adapter = adapter
        
        if (isUpcoming) {
            upcomingEventsAdapter = adapter
            events.addAll(listOf(
                Event("International Band Mu...", "Foster Hall, Basement", "10\nJUNE", R.drawable.ic_launcher_background),
                Event("Jo Malone", "Radius Gallery", "10\nJUNE", R.drawable.ic_launcher_background),
                Event("Art Club Mixer", "Singleton Center", "12\nJUNE", R.drawable.ic_launcher_background),
                Event("Study Group Session", "Johnston Memorial Library", "14\nJUNE", R.drawable.ic_launcher_background),
                Event("Outdoor Movie Night", "The VSU Quad", "15\nJUNE", R.drawable.ic_launcher_background)
            ))
        } else {
            // Add filler data for current and past events for now
             events.addAll(listOf(
                Event("Sample Event 1", "Sample Location", "1\nJAN", R.drawable.ic_launcher_background),
                Event("Sample Event 2", "Sample Location", "2\nFEB", R.drawable.ic_launcher_background)
            ))
        }
        
        adapter.notifyDataSetChanged()
    }
}
