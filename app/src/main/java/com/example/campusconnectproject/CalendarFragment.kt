package com.example.campusconnectproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.campusconnectproject.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val events = mutableListOf<String>()

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

        // Set initial date text
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        binding.selectedDateText.text = "Selected Date: ${sdf.format(calendar.time)}"

        // Add initial event for demonstration and update the list
        if (events.isEmpty()) { // Add only if the list is empty to prevent duplicates
            events.add("- Sí se Puede International Potluck !")
            binding.eventsListTextView.text = events.joinToString("\n")
        }

        // Listener for date changes
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            binding.selectedDateText.text = "Selected Date: ${sdf.format(calendar.time)}"
        }

        // Listener for the add event button
        binding.addEventButton.setOnClickListener {
            val eventTitle = binding.eventTitleEditText.text.toString()
            if (eventTitle.isNotEmpty()) {
                events.add("- $eventTitle")
                binding.eventsListTextView.text = events.joinToString("\n")
                binding.eventTitleEditText.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}