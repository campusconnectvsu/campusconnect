package com.example.campusconnectproject

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings


class CampusConnectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupFirestoredb()
    }


    // set up firbase to store data in cache
    private fun setupFirestoredb() {
        // set up firestore
        val datab = FirebaseFirestore.getInstance()

        // apply memory cache settings
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build()).build()
        datab.firestoreSettings = settings
    }
}
