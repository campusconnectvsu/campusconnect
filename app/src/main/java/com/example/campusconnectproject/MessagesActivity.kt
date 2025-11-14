package com.example.campusconnectproject

import com.example.campusconnectproject.Chat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessagesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        recyclerView = findViewById(R.id.recyclerViewChats)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val chats = listOf(
            Chat("Fauziah", "I will do the voice over", "10:30 PM", R.drawable.ic_launcher_background, true),
            Chat("Nicole", "just open la🥺🥺", "3:15 PM", R.drawable.ic_launcher_background, true),
            Chat("Brian", "bye 😊", "Yesterday", R.drawable.ic_launcher_background, false),
            Chat("Cheng", "call me when you get...", "Yesterday", R.drawable.ic_launcher_background, false),
            Chat("Model", "ready for another adv...", "Yesterday", R.drawable.ic_launcher_background, false),
            Chat("Ash King", "whatsapp my frnd🐯", "2d", R.drawable.ic_launcher_background, false),
            Chat("Remote Guy", "here is your bill for the...", "Mar 10", R.drawable.ic_launcher_background, false)
        )

        adapter = ChatAdapter(chats)
        recyclerView.adapter = adapter
    }
}
