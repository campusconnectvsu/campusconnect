package com.example.campusconnectproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private const val VIEW_TYPE_SENT = 1
private const val VIEW_TYPE_RECEIVED = 2

class ConversationAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<ConversationAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.messageText)
        val messageImage: ImageView = view.findViewById(R.id.messageImage)
        val messageTime: TextView = view.findViewById(R.id.messageTime)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSent) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = if (viewType == VIEW_TYPE_SENT) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        
        // Format timestamp to readable time
        val sdf = java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault())
        val timeString = sdf.format(java.util.Date(if (message.timestamp == 0L) System.currentTimeMillis() else message.timestamp))
        holder.messageTime.text = timeString

        if (message.imageUrl != null) {
            holder.messageImage.visibility = View.VISIBLE
            holder.messageText.visibility = View.GONE
            Glide.with(holder.itemView.context)
                .load(message.imageUrl)
                .into(holder.messageImage)
        } else {
            holder.messageImage.visibility = View.GONE
            holder.messageText.visibility = View.VISIBLE
            holder.messageText.text = message.text
        }
    }

    override fun getItemCount() = messages.size
}
