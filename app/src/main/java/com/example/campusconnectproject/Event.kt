package com.example.campusconnectproject

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val title: String,
    val location: String,
    val date: String,
    @DrawableRes val imageResId: Int,
    val organizer: String = "Campus Connect",
    val description: String = "No description available."
) : Parcelable
