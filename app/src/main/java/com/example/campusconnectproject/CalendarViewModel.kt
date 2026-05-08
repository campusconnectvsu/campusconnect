package com.example.campusconnectproject

import androidx.lifecycle.ViewModel
class CalendarViewModel : ViewModel() {
    // stores manually added events by user
    val evByDate = mutableMapOf<String, MutableList<Event>>()
}