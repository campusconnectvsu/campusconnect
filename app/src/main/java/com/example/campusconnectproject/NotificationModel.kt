package com.example.campusconnectproject

import java.io.Serializable

data class NotificationModel(
    val id: String,
    val title: String,
    val body: String,
    val timestamp: Long,
    val type: String = "general", // "event", "message", "alert"
    val isRead: Boolean = false
) : Serializable
