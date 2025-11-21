package com.example.campusconnectproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventImage: ImageView = view.findViewById(R.id.eventImage)
        val eventDate: TextView = view.findViewById(R.id.eventDate)
        val eventTitle: TextView = view.findViewById(R.id.eventTitle)
        val eventLocation: TextView = view.findViewById(R.id.eventLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventImage.setImageResource(event.imageResId)
        holder.eventDate.text = event.date
        holder.eventTitle.text = event.title
        holder.eventLocation.text = event.location
    }

    override fun getItemCount() = events.size
}
