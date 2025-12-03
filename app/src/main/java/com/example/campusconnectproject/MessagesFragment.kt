package com.example.campusconnectproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.FragmentMessagesBinding

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

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

        binding.recyclerViewChats.layoutManager = LinearLayoutManager(context)

        val chats = listOf(
            Chat("Fauziah", "I will do the voice over", "10:30 PM", R.drawable.ic_launcher_background, true),
            Chat("Nicole", "just open la🥺🥺", "3:15 PM", R.drawable.ic_launcher_background, true),
            Chat("Brian", "bye 😊", "Yesterday", R.drawable.ic_launcher_background, false),
            Chat("Cheng", "call me when you get...", "Yesterday", R.drawable.ic_launcher_background, false),
            Chat("Model", "ready for another adv...", "Yesterday", R.drawable.ic_launcher_background, false),
            Chat("Ash King", "whatsapp my frnd🐯", "2d", R.drawable.ic_launcher_background, false),
            Chat("Remote Guy", "here is your bill for the...", "Mar 10", R.drawable.ic_launcher_background, false)
        )

        binding.recyclerViewChats.adapter = ChatAdapter(chats)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}