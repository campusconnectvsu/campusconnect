package com.example.campusconnectproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.ActivityNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class NotificationsActivity : AppCompatActivity() {

    // binding for notifications layout
    private lateinit var binding: ActivityNotificationsBinding
    // firebase instance
    private val db = FirebaseFirestore.getInstance()
    // auth instance
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        loadNotifications()
    }

    // setup toolbar for back button
    private fun setupToolbar() {
        setSupportActionBar(binding.returnBackBtn)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.returnBackBtn.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    // update notifications
    private fun loadNotifications() {
        // get user id
        val uid = auth.currentUser?.uid?: return
        // query for notifications
        db.collection("notifications").document(uid)
            .collection("items")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if ( error != null || snapshots == null ) return@addSnapshotListener
                // map firestore doc to notification
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
                // bind the notifications to the recycler view
                binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.notificationsRecyclerView.adapter = NotificationAdapter(notifications)
                // mark unread notification as read
                for ( doc in snapshots.documents){
                    if (doc.getBoolean("isRead") == false ){
                        doc.reference.update("isRead", true)
                    }
                }

            }

    }

    companion object{
        // send notification to user
        fun sendNotification(
            db: FirebaseFirestore,
            targetUid: String,
            title: String,
            body : String,
            type: String
        ){
            // create and send notification data
            val noti_Data = hashMapOf("title" to title, "body" to body, "type" to type, "timestamp" to System.currentTimeMillis(),"isRead" to false)
            db.collection("notifications").document(targetUid)
                .collection("items")
                .add(noti_Data)
        }
    }
}
