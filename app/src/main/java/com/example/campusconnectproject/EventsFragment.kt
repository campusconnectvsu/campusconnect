package com.example.campusconnectproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create sample data
        val upcomingEvents = listOf(
            Event("International Band Mu...", "Foster Hall, Basement", "10 JUNE", R.drawable.ic_event_placeholder),
            Event("Jo Malone", "Radius Gallery", "10 JUNE", R.drawable.ic_event_placeholder)
        )

        val currentEvents = listOf(
            Event("Sample Event 1", "Sample Location", "1 JAN", R.drawable.ic_event_placeholder),
            Event("Sample Event 2", "Sample Location", "2 FEB", R.drawable.ic_event_placeholder)
        )

        val pastEvents = listOf(
            Event("Sample Event 1", "Sample Location", "1 JAN", R.drawable.ic_event_placeholder),
            Event("Sample Event 2", "Sample Location", "2 FEB", R.drawable.ic_event_placeholder)
        )

        // Setup RecyclerViews
        binding.upcomingEventsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.upcomingEventsRecyclerView.adapter = EventAdapter(upcomingEvents)

        binding.currentEventsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.currentEventsRecyclerView.adapter = EventAdapter(currentEvents)

        binding.pastEventsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.pastEventsRecyclerView.adapter = EventAdapter(pastEvents)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}