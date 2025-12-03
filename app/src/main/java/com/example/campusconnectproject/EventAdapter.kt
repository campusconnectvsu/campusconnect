package com.example.campusconnectproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnect.EventDetailsActivity
import com.example.campusconnectproject.databinding.ItemEventBinding

class EventAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventImage.setImageResource(event.imageResId)
            binding.eventDate.text = event.date
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location

            // Set a click listener on the event card to go to the new details screen
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, EventDetailsActivity::class.java)
                context.startActivity(intent)
            }

            // Set a click listener on the button to go to your original edit screen
            binding.viewDetailsButton.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, EditEventActivity::class.java).apply {
                    putExtra("EVENT_TITLE", event.title)
                    putExtra("EVENT_LOCATION", event.location)
                    putExtra("EVENT_DATE", event.date)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size
}
