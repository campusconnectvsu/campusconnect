package com.example.campusconnectproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnectproject.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(private val notifications: List<NotificationModel>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

        // view holder for notification item
    class NotificationViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        // inflate view
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.binding.apply {
            // bind data to views
            notificationTitle.text = notification.title
            notificationBody.text = notification.body

            // format timestamp and date into string
            val sdf = SimpleDateFormat("h:mm a, MMM d", Locale.getDefault())
            notificationTime.text = sdf.format(Date(notification.timestamp))

            // show/hide dot based on read status
            unreadDot.visibility = if (notification.isRead) View.GONE else View.VISIBLE
            
            // Highlight based on type
            when (notification.type) {
                "event" -> notificationIcon.setImageResource(R.drawable.ic_calendar)
                "message" -> notificationIcon.setImageResource(R.drawable.ic_messages)
                else -> notificationIcon.setImageResource(R.drawable.ic_notifications)
            }
        }
    }

    override fun getItemCount() = notifications.size
}
