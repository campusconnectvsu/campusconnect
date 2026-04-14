package com.example.campusconnectproject

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.ActivityMessagesBinding

import android.widget.Toast
import androidx.appcompat.widget.SearchView

class MessagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessagesBinding
    private lateinit var adapter: ChatAdapter
    private lateinit var onlineAdapter: OnlineFriendAdapter
    private var allChats = listOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Only apply padding to the AppBarLayout instead of the whole root
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.recyclerViewChats.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOnline.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Updated sample data to use ic_person instead of the green launcher background
        allChats = listOf(
            Chat("Fauziah", "I will do the voice over", "10:30 PM", R.drawable.ic_person, true, 2),
            Chat("Nicole", "just open la🥺🥺", "3:15 PM", R.drawable.ic_person, true, 5),
            Chat("Brian", "bye 😊", "Yesterday", R.drawable.ic_person, false),
            Chat("Cheng", "call me when you get...", "Yesterday", R.drawable.ic_person, false),
            Chat("Model", "ready for another adv...", "Yesterday", R.drawable.ic_person, false),
            Chat("Ash King", "whatsapp my frnd🐯", "2d", R.drawable.ic_person, false),
            Chat("Remote Guy", "here is your bill for the...", "Mar 10", R.drawable.ic_person, false)
        )

        adapter = ChatAdapter(allChats)
        binding.recyclerViewChats.adapter = adapter

        val onlineFriends = allChats.filter { it.isOnline }
        onlineAdapter = OnlineFriendAdapter(onlineFriends)
        binding.recyclerViewOnline.adapter = onlineAdapter

        // --- Search Logic --- //
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterChats(newText)
                return true
            }
        })

        // --- New Chat Button --- //
        binding.btnNewChat.setOnClickListener {
            Toast.makeText(this, "Start a new conversation!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterChats(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            allChats
        } else {
            allChats.filter { it.name.contains(query, ignoreCase = true) }
        }
        binding.recyclerViewChats.adapter = ChatAdapter(filteredList)
    }
}
