package com.example.campusconnectproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.ActivityNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadNotifications()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.returnBackBtn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.returnBackBtn.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadNotifications() {
        val uid = auth.currentUser?.uid?: return
        db.collection("notifications").document(uid)
            .collection("items")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if ( error != null || snapshots == null ) return@addSnapshotListener
                val notifications = snapshots.documents.mapNotNull { doc ->
                    NotificationModel(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        body = doc.getString("body") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L,
                        type = doc.getString("type")?: "general",
                        isRead = doc.getBoolean("isRead") ?: false
                    )
                }
                binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.notificationsRecyclerView.adapter = NotificationAdapter(notifications)
                for ( doc in snapshots.documents){
                    if (doc.getBoolean("isRead") == false ){
                        doc.reference.update("isRead", true)
                    }
                }

            }

    }

    companion object{
        fun sendNotification(
            db: FirebaseFirestore,
            targetUid: String,
            title: String,
            body : String,
            type: String
        ){
            val noti_Data = hashMapOf("title" to title, "body" to body, "type" to type, "timestamp" to System.currentTimeMillis(),"isRead" to false)
            db.collection("notifications").document(targetUid)
                .collection("items")
                .add(noti_Data)
        }
    }
}
