package com.example.campusconnectproject

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

class CampusConnectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupFirestoreOfflinePersistence()
    }

    private fun setupFirestoreOfflinePersistence() {
        val db = FirebaseFirestore.getInstance()
        
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder()
                // Set the cache size to 100 MB (optional)
                .setSizeBytes(100L * 1024L * 1024L)
                .build())
            .build()
            
        db.firestoreSettings = settings
    }
}
