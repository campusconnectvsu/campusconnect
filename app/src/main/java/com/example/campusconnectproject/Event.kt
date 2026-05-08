package com.example.campusconnectproject

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    // title of event
    val title: String,

    // location of event
    val location: String,

    // date of event
    val date: String,
    // drawable event image id
    @DrawableRes val imageResId: Int,
    // name of event org
    val organizer: String = "Campus Connect",
    // description of event
    val description: String = "No description available."
) : Parcelable
