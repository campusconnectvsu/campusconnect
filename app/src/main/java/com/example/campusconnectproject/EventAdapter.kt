package com.example.campusconnectproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnectproject.databinding.ItemEventBinding

class EventAdapter(private val events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventImage.setImageResource(event.imageResId)
            binding.eventDate.text = event.date
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location

            val context = itemView.context
            if (EventManager.isJoined(event)) {
                binding.viewDetailsButton.text = "Joined"
                binding.viewDetailsButton.setTextColor(context.getColor(R.color.white))
                binding.viewDetailsButton.backgroundTintList = android.content.res.ColorStateList.valueOf(context.getColor(R.color.success_green))
            } else {
                binding.viewDetailsButton.text = "View Details"
                binding.viewDetailsButton.setTextColor(context.getColor(R.color.primary_blue))
                
                // Use a themed background color for the button
                val buttonBg = if (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                    context.getColor(R.color.button_bg_dark)
                } else {
                    context.getColor(R.color.button_bg_light)
                }
                binding.viewDetailsButton.backgroundTintList = android.content.res.ColorStateList.valueOf(buttonBg)
            }

            // Helper to open details
            val openDetails = {
                val intent = Intent(context, EventDetailsActivity::class.java).apply {
                    putExtra("EXTRA_EVENT", event)
                }
                context.startActivity(intent)
            }

            // Set click listeners
            itemView.setOnClickListener { openDetails() }
            binding.viewDetailsButton.setOnClickListener { openDetails() }

            // Handle Favorite Toggle
            var isFavorite = false // This should ideally come from a database/manager later
            binding.btnFavorite.setOnClickListener {
                isFavorite = !isFavorite
                binding.btnFavorite.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite_border // Change to ic_favorite if you have it
                    else R.drawable.ic_favorite_border
                )
                val tint = if (isFavorite) R.color.error_red else R.color.text_secondary_light
                binding.btnFavorite.imageTintList = android.content.res.ColorStateList.valueOf(context.getColor(tint))
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
