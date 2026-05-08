package com.example.campusconnectproject

data class Chat(
    // display name of user
    val name: String,
    // last message sent
    val message: String,
    // timestamp of last message
    val time: String,
    // prof pic of user
    val imageResId: Int,
    // whether user is online
    val isOnline: Boolean,
    // number of unread messages
    val unreadCount: Int = 0,
    // Firestore UiD of the other participants
    val receiverId: String = ""
)
