package com.example.campusconnectproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.ActivityAllEventsBinding

class AllEventsActivity : AppCompatActivity() {

    // binding all event layout xml
    private lateinit var binding: ActivityAllEventsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get categ intent from previous page
        val category = intent.getStringExtra("CATEGORY") ?: "Events"
        binding.headerTitle.text = category

        // go back to previous page
        binding.backButton.setOnClickListener { finish() }

        // load event based on categ selection
        val events = when (category) {
            "Upcoming Events" -> listOf(
                Event("International Band Mu...", "Foster Hall, Basement", "10 JUNE", R.drawable.vsu_logo),
                Event("Jo Malone", "Radius Gallery", "10 JUNE", R.drawable.vsu_logo),
                Event("Art Club Mixer", "Singleton Center", "12 JUNE", R.drawable.vsu_logo)
            )
            "Current Events" -> listOf(
                Event("Sample Event 1", "Sample Location", "1 JAN", R.drawable.vsu_logo),
                Event("Sample Event 2", "Sample Location", "2 FEB", R.drawable.vsu_logo)
            )
            else -> listOf(
                Event("Past Event 1", "Old Location", "1 DEC", R.drawable.vsu_logo),
                Event("Past Event 2", "Old Location", "5 DEC", R.drawable.vsu_logo)
            )
        }

        // setup recycler view
        binding.allEventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AllEventsActivity)
            adapter = EventAdapter(events)
        }
    }
}
