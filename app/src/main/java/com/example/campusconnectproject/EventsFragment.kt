package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnectproject.databinding.FragmentEventsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    private val fireB_db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
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
        Unread_Noti()

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

        // Set up "See All" click listeners
        binding.seeAllUpcoming.setOnClickListener {
            openAllEvents("Upcoming Events")
        }
        binding.seeAllCurrent.setOnClickListener {
            openAllEvents("Current Events")
        }
        binding.seeAllPast.setOnClickListener {
            openAllEvents("Past Events")
        }
    }

    private fun openAllEvents(category: String) {
        val intent = Intent(requireContext(), AllEventsActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }

    private fun Unread_Noti(){
        val uid = auth.currentUser?.uid?: return
        fireB_db.collection("notifications").document(uid)
            .collection("items")
            .whereEqualTo("isRead", false)
            .addSnapshotListener (MetadataChanges.INCLUDE){ snapshots, error ->
                if (error != null ) {
                    Log.e("Bell", "Error: ${error?.message}")
                    return@addSnapshotListener
                }
                Log.d("Bell", "Snapshot size: ${snapshots?.size()}")
                if (_binding == null) return@addSnapshotListener
                val hasUnread =(snapshots?.size() ?:0) >0
                binding.notificationBell.imageTintList = android.content.res.ColorStateList.valueOf(
                    if(hasUnread) android.graphics.Color.RED
                    else android.graphics.Color.DKGRAY
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
