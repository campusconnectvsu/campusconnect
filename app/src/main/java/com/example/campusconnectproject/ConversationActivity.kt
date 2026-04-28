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

class ConversationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversationBinding


    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val messages = mutableListOf<Message>()
    private lateinit var adapter: ConversationAdapter
    private var prof_Name: String = ""
    private var rece_Id: String = ""
    private var messeges_Id: String = ""

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { send_Dms(imageUrl = it.toString()) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        prof_Name = intent.getStringExtra("chat_name") ?: ""
        rece_Id = intent.getStringExtra("receiver_id") ?: ""

        binding.chatName.text = prof_Name
        binding.backBtn.setOnClickListener { finish() }

        val currentUid = auth.currentUser?.uid ?: return
        messeges_Id = if (currentUid < rece_Id) {
            "${currentUid}_${currentUid}"
        } else {
            "${rece_Id}_${currentUid}"
        }
        adapter = ConversationAdapter(messages)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true


        binding.conversationRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.conversationRecyclerView.adapter = adapter

        listenFor_Dms()

        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                send_Dms(text = text)
                binding.messageEditText.text?.clear()
            }
        }

        binding.attachButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun send_Dms(text: String? = null, imageUrl: String? = null) {
        val currentUid = auth.currentUser?.uid ?: return
        val Dms = hashMapOf(
            "text" to text,
            "imageUrl" to imageUrl,
            "senderId" to currentUid,
            "receiverId" to rece_Id,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("conversations").document(messeges_Id). collection("messages") .add(Dms)
    }


    private fun listenFor_Dms() {
        val currentUid = auth.currentUser?.uid ?: return
        db.collection("conversations").document(messeges_Id).collection("messages")
            .orderBy("timeStamp", Query.Direction.ASCENDING)
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
                            timestamp = doc.getLong("timeStamp") ?: 0L

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
