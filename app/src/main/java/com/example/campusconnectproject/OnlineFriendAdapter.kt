package com.example.campusconnectproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OnlineFriendAdapter(private val friendList: List<Chat>) :
    RecyclerView.Adapter<OnlineFriendAdapter.ViewHolder>() {

        // holds profile image and name
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProfile: ImageView = view.findViewById(R.id.imageProfile)
        val textNameShort: TextView = view.findViewById(R.id.textNameShort)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate online friend layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_online_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind data to views and set click listener
        val friend = friendList[position]
        holder.textNameShort.text = friend.name.split(" ")[0]
        holder.imageProfile.setImageResource(friend.imageResId)
        // set click listener
        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount() = friendList.size
}
