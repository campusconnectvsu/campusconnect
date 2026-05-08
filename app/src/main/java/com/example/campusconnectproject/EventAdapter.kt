package com.example.campusconnectproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnectproject.databinding.ItemEventBinding

class EventAdapter(initialList: List<Event>? = null) : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    init {
        // submit default list to adapter
        initialList?.let { submitList(it) }
    }

    // compares events for changes
    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            // Ideally, use a unique ID here (like a Firestore document ID)
            return oldItem.title == newItem.title && oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    // holder for event items
    inner class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        // binds data to card view
        fun bind(event: Event) {
            binding.eventImage.setImageResource(event.imageResId)
            binding.eventDate.text = event.date
            binding.eventTitle.text = event.title
            binding.eventLocation.text = event.location

            val context = itemView.context
            // change btn based on join status
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
        // inflate card view
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
