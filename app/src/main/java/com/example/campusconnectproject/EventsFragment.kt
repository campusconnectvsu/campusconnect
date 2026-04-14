package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnectproject.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    
    // This pool will be shared between all three RecyclerViews
    private val viewPool = RecyclerView.RecycledViewPool()

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

        binding.notificationBell.setOnClickListener {
            val intent = Intent(requireContext(), NotificationsActivity::class.java)
            startActivity(intent)
        }

        // Create sample data with the new VSU logo
        val upcomingEvents = listOf(
            Event("International Band Mu...", "Foster Hall, Basement", "10 JUNE", R.drawable.vsu_logo),
            Event("Jo Malone", "Radius Gallery", "10 JUNE", R.drawable.vsu_logo)
        )

        val currentEvents = listOf(
            Event("Sample Event 1", "Sample Location", "1 JAN", R.drawable.vsu_logo),
            Event("Sample Event 2", "Sample Location", "2 FEB", R.drawable.vsu_logo)
        )

        val pastEvents = listOf(
            Event("Sample Event 1", "Sample Location", "1 JAN", R.drawable.vsu_logo),
            Event("Sample Event 2", "Sample Location", "2 FEB", R.drawable.vsu_logo)
        )

        // Setup Upcoming RecyclerView
        binding.upcomingEventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EventAdapter(upcomingEvents)
            setRecycledViewPool(viewPool) // Optimization
            setHasFixedSize(true)         // Optimization
        }

        // Setup Current RecyclerView
        binding.currentEventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EventAdapter(currentEvents)
            setRecycledViewPool(viewPool) // Optimization
            setHasFixedSize(true)         // Optimization
        }

        // Setup Past RecyclerView
        binding.pastEventsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = EventAdapter(pastEvents)
            setRecycledViewPool(viewPool) // Optimization
            setHasFixedSize(true)         // Optimization
        }

        // Observe EventManager to refresh UI when joining/leaving events
        EventManager.joinedEvents.observe(viewLifecycleOwner) {
            binding.upcomingEventsRecyclerView.adapter?.notifyDataSetChanged()
            binding.currentEventsRecyclerView.adapter?.notifyDataSetChanged()
            binding.pastEventsRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
