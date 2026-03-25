package com.example.campusconnectproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class UsersAdapter(
    // List of users to display
    private var users: List<User>
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    // ViewHolder class for each user item
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameView: TextView = view.findViewById(R.id.userName)


    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_user, parent, false)
        return UserViewHolder(view)


    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int) {
        val user = users[position]
        viewHolder.userNameView.text = user.name

        // Set click listener when user clicks on the profile
        viewHolder.itemView.setOnClickListener {
            val context = viewHolder.itemView.context
            val intent = Intent(context, UserProfActivity::class.java)
            intent.putExtra("USER_ID", user.uid)
            context.startActivity(intent)
        }

    }

    // Return the number of users
    override fun getItemCount(): Int {
        return users.size

    }

    // Update the list of users
    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }


}