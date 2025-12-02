package com.example.campusconnectproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.neweventdetails.MainActivity as NewEventDetailsActivity

class EventAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventImage: ImageView = view.findViewById(R.id.eventImage)
        val eventDate: TextView = view.findViewById(R.id.eventDate)
        val eventTitle: TextView = view.findViewById(R.id.eventTitle)
        val eventLocation: TextView = view.findViewById(R.id.eventLocation)
        val viewDetailsButton: Button = view.findViewById(R.id.viewDetailsButton)
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

        // Set a click listener on the event card to go to the new details screen
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NewEventDetailsActivity::class.java)
            context.startActivity(intent)
        }

        // Set a click listener on the button to go to your original edit screen
        holder.viewDetailsButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditEventActivity::class.java).apply {
                putExtra("EVENT_TITLE", event.title)
                putExtra("EVENT_LOCATION", event.location)
                putExtra("EVENT_DATE", event.date)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = events.size
}
