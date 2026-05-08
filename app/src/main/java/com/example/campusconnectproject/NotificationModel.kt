package com.example.campusconnectproject

import java.io.Serializable
// data model for notifications
data class NotificationModel(
    val id: String,
    val title: String,
    val body: String,
    val timestamp: Long,
    val type: String = "general",
    val isRead: Boolean = false
) : Serializable
