package com.example.campusconnectproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var adapter: ChatAdapter
    private lateinit var onlineAdapter: OnlineFriendAdapter
    private var allChats = listOf<Chat>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        loadData()
        setupSearch()
        
        binding.btnNewChat.setOnClickListener {
            Toast.makeText(context, "Start a new conversation!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewChats.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewOnline.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadData() {
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
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterChats(newText)
                return true
            }
        })
    }

    private fun filterChats(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            allChats
        } else {
            allChats.filter { it.name.contains(query, ignoreCase = true) }
        }
        binding.recyclerViewChats.adapter = ChatAdapter(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
