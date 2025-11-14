package com.example.campusconnectproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatList: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProfile: ImageView = view.findViewById(R.id.imageProfile)
        val textName: TextView = view.findViewById(R.id.textName)
        val textMessage: TextView = view.findViewById(R.id.textMessage)
        val textTime: TextView = view.findViewById(R.id.textTime)
        val onlineDot: View = view.findViewById(R.id.onlineDot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]
        holder.textName.text = chat.name
        holder.textMessage.text = chat.message
        holder.textTime.text = chat.time
        holder.imageProfile.setImageResource(chat.imageResId)
        holder.onlineDot.visibility = if (chat.isOnline) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra("chat_name", chat.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = chatList.size
}
