package com.example.campusconnectproject

import androidx.annotation.DrawableRes

data class Event(
    val title: String,
    val location: String,
    val date: String,
    @DrawableRes val imageResId: Int
)
