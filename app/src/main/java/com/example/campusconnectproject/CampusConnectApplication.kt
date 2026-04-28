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


    private fun setupFirestoredb() {
        val datab = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build()).build()
        datab.firestoreSettings = settings
    }
}
