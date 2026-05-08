package com.example.campusconnectproject

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusconnectproject.databinding.ActivityConversationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class ConversationActivity : AppCompatActivity() {

    // Binding for the conversation layout
    private lateinit var binding: ActivityConversationBinding
    // Firestore db instance
    private val db = FirebaseFirestore.getInstance()
    // Firebase auth instance
    private val auth = FirebaseAuth.getInstance()
    // List of messages
    private val messages = mutableListOf<Message>()
    // adapter for message list
    private lateinit var adapter: ConversationAdapter
    // names of users
    private var prof_Name: String = ""
    // ids of receiver
    private var rece_Id: String = ""
    // id of conversation
    private var messeges_Id: String = ""

    // Launcher for image selection
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { send_Dms(imageUrl = it.toString()) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get other users names and ids from intent
        prof_Name = intent.getStringExtra("chat_name") ?: ""
        rece_Id = intent.getStringExtra("receiver_id") ?: ""

        binding.uChatName.text = prof_Name
        binding.returnBackBtn.setOnClickListener { finish() }

        // get current user UID
        val currentUid = auth.currentUser?.uid ?: return
        // build a unique id for the conversation
        messeges_Id = if (currentUid < rece_Id) {
            "${currentUid}_${rece_Id}"
        } else {
            "${rece_Id}_${currentUid}"
        }
        // set up recycler view for messages
        adapter = ConversationAdapter(messages)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true

        binding.conversationRecyclerView.layoutManager = layoutManager
        binding.conversationRecyclerView.adapter = adapter

        listenFor_Dms()

        // send message when btn is clicked
        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                send_Dms(text = text)
                binding.messageEditText.text?.clear()
            }
        }

        // open image picker when btn is clicked
        binding.attachButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    // send message to firebase
    private fun send_Dms(text: String? = null, imageUrl: String? = null) {
        // get current user UID
        val currentUid = auth.currentUser?.uid ?: return
        // update conversation in db
        db.collection("conversations").document(messeges_Id)
            .set(
                hashMapOf(
                    "participants" to listOf(currentUid, rece_Id),
                    "lastMessage" to (text ?: ""),
                    "timestamp" to System.currentTimeMillis()
                ),
                SetOptions.merge()
            )

        // build a message map and add to db
        val Dms = hashMapOf(
            "text" to text,
            "imageUrl" to imageUrl,
            "senderId" to currentUid,
            "receiverId" to rece_Id,
            "timestamp" to System.currentTimeMillis()
        )
        // add message to db
        db.collection("conversations").document(messeges_Id).collection("messages").add(Dms)
        // send notification to receiver
        db. collection("users").document(currentUid).get()
            .addOnSuccessListener { doc ->
                val cur_UName = doc.getString("name")?.replaceFirstChar { it.uppercase() }
                NotificationsActivity.sendNotification(
                    db = db,
                    targetUid = rece_Id,
                    title = " New Message from $cur_UName",
                    body = text?: "Sent you an image",
                    type = "message"
                )
            }

    }
    // listen for changes in messages
    private fun listenFor_Dms() {
        // get current user UID
        val currentUid = auth.currentUser?.uid ?: return
        db.collection("conversations").document(messeges_Id).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener
                messages.clear()
                for (doc in snapshot.documents) {
                    val senderId = doc.getString("senderId") ?: ""
                    messages.add(
                        Message(
                            text = doc.getString("text"),
                            imageUrl = doc.getString("imageUrl"),
                            isSent = senderId == currentUid,
                            senderId = senderId,
                            receiverId = doc.getString("receiverId") ?: "",
                            timestamp = doc.getLong("timestamp") ?: 0L

                        )
                    )
                }
                adapter.updateMessages(messages.toList())
                if (messages.isNotEmpty()) {
                    binding.conversationRecyclerView.scrollToPosition(messages.size - 1)
                }
            }
    }
}
