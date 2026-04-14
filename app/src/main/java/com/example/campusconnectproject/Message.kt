package com.example.campusconnectproject

data class Message(
    val text: String? = null,
    val imageUrl: String? = null,
    val isSent: Boolean = false,
    val senderId: String = "",
    val receiverId: String = "",
    val timestamp: Long = 0L
)
