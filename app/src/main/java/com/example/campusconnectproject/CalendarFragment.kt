package com.example.campusconnectproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    
    // Key: Internal Date string ("yyyy-MM-dd"), Value: List of events
    private val eventsByDate = mutableMapOf<String, MutableList<Event>>()
    private var selectedDateKey: String = ""

    // Formatters for storage and display
    private val storageSdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displaySdf = SimpleDateFormat("MMMM d, yyyy", Locale.US)
    private val eventDateSdf = SimpleDateFormat("d MMMM", Locale.US) // For "10 JUNE" format

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarEventsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Set initial date to today
        val calendar = Calendar.getInstance()
        updateSelectedDate(calendar)

        // Observe joined events from EventManager
        EventManager.joinedEvents.observe(viewLifecycleOwner) {
            updateUIForSelectedDate()
        }

        // Listener for date changes
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            updateSelectedDate(calendar)
        }

        // Listener for the add event button
        binding.addEventButton.setOnClickListener {
            val eventTitle = binding.eventTitleEditText.text.toString().trim()
            if (eventTitle.isNotEmpty()) {
                val eventsForDate = eventsByDate.getOrPut(selectedDateKey) { mutableListOf() }
                
                // Create a new Event object
                val newEvent = Event(
                    title = eventTitle,
                    location = "Campus", // Default location
                    date = binding.selectedDateText.text.toString(),
                    imageResId = R.drawable.vsu_logo // Default image
                )
                
                eventsForDate.add(newEvent)
                
                updateUIForSelectedDate()
                binding.eventTitleEditText.text?.clear()
            }
        }
    }

    private fun updateSelectedDate(calendar: Calendar) {
        selectedDateKey = storageSdf.format(calendar.time)
        binding.selectedDateText.text = displaySdf.format(calendar.time)
        updateUIForSelectedDate()
    }

    private fun updateUIForSelectedDate() {
        val manualEvents = eventsByDate[selectedDateKey] ?: mutableListOf()
        
        // Convert the selectedDateKey back to a Date to compare with Event dates
        val selectedDate = storageSdf.parse(selectedDateKey)
        val eventDateFormat = selectedDate?.let { eventDateSdf.format(it).uppercase() } ?: ""

        val joinedEvents = EventManager.joinedEvents.value.orEmpty().filter { 
            val normalizedEventDate = it.date.replace("\n", " ").uppercase()
            normalizedEventDate == eventDateFormat
        }

        val allEvents = manualEvents + joinedEvents
        binding.calendarEventsRecyclerView.adapter = EventAdapter(allEvents)
        
        // Show/Hide empty state
        if (allEvents.isEmpty()) {
            binding.emptyStateContainer.visibility = View.VISIBLE
            binding.calendarEventsRecyclerView.visibility = View.GONE
        } else {
            binding.emptyStateContainer.visibility = View.GONE
            binding.calendarEventsRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
