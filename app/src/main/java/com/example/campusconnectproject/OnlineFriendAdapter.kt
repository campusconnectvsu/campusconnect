package com.example.campusconnectproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OnlineFriendAdapter(private val friendList: List<Chat>) :
    RecyclerView.Adapter<OnlineFriendAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProfile: ImageView = view.findViewById(R.id.imageProfile)
        val textNameShort: TextView = view.findViewById(R.id.textNameShort)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_online_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friendList[position]
        holder.textNameShort.text = friend.name.split(" ")[0]
        holder.imageProfile.setImageResource(friend.imageResId)
        
        holder.itemView.setOnClickListener {
            // Handle click if needed
        }
    }

    override fun getItemCount() = friendList.size
}
