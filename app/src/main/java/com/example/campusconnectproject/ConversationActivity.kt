package com.example.campusconnectproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.ActivityConversationBinding

class ConversationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversationBinding

    private val messages = mutableListOf(
        Message("Hello!", false),
        Message("Hi, how are you?", true),
        Message("I'm good, thanks!", false)
    )
    private val adapter = ConversationAdapter(messages)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val chatName = intent.getStringExtra("chat_name")
        supportActionBar?.title = chatName

        binding.conversationRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.conversationRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString()
            if (text.isNotEmpty()) {
                val message = Message(text, true) // true because it's a sent message
                messages.add(message)
                adapter.notifyItemInserted(messages.size - 1)
                binding.conversationRecyclerView.scrollToPosition(messages.size - 1)
                binding.messageEditText.text.clear()
            }
        }
    }
}
