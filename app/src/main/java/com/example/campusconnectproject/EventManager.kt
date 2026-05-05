package com.example.campusconnectproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EventManager {
    private val _joinedEvents = MutableLiveData<List<Event>>(emptyList())
    val joinedEvents: LiveData<List<Event>> = _joinedEvents

    fun joinEvent(event: Event) {
        val currentList = _joinedEvents.value.orEmpty().toMutableList()
        if (!currentList.any { it.title == event.title && it.date == event.date }) {
            currentList.add(event)
            _joinedEvents.value = currentList
        }
    }

    fun leaveEvent(event: Event) {
        val currentList = _joinedEvents.value.orEmpty().toMutableList()
        currentList.removeAll { it.title == event.title && it.date == event.date }
        _joinedEvents.value = currentList
    }

    fun isJoined(event: Event): Boolean {
        return _joinedEvents.value.orEmpty().any { it.title == event.title && it.date == event.date }
    }

    fun  clearAll(){
        _joinedEvents.value = emptyList()
    }
}