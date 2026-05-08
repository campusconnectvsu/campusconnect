package com.example.campusconnectproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EventManager {
    // list of joined events
    private val _joinedEvents = MutableLiveData<List<Event>>(emptyList())
    // live data for joined events
    val joinedEvents: LiveData<List<Event>> = _joinedEvents

    // adds an event to the list
    fun joinEvent(event: Event) {
        val currentList = _joinedEvents.value.orEmpty().toMutableList()
        if (!currentList.any { it.title == event.title && it.date == event.date }) {
            currentList.add(event)
            _joinedEvents.value = currentList
        }
    }

    // removes an event from the list
    fun leaveEvent(event: Event) {
        val currentList = _joinedEvents.value.orEmpty().toMutableList()
        currentList.removeAll { it.title == event.title && it.date == event.date }
        _joinedEvents.value = currentList
    }

    // checks if an event is joined
    fun isJoined(event: Event): Boolean {
        return _joinedEvents.value.orEmpty().any { it.title == event.title && it.date == event.date }
    }

    // clear all joined events
    fun  clearAll(){
        _joinedEvents.value = emptyList()
    }
}