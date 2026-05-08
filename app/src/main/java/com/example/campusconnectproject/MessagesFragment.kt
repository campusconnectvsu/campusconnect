package com.example.campusconnectproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusconnectproject.databinding.FragmentMessagesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source

class MessagesFragment : Fragment() {

    // view binding messages layout
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    // adapter for chat list and online friends
    private lateinit var adapter: ChatAdapter
    private lateinit var onlineAdapter: OnlineFriendAdapter
    // full list of chats
    private var allChats = listOf<Chat>()

    // firestore db instance and auth instance
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        loadRalConversations()
        setupSearch()

        // hide online friends recycler view and show new message button
        binding.recyclerViewOnline.visibility = View.GONE
        binding.btnNewChat.setOnClickListener {
            showNewMessageBottomSheet()
        }
    }

    // show bottom sheet for new message
    private fun showNewMessageBottomSheet() {
        // get current user UID
        val currentUid = auth.currentUser?.uid ?: return
        val dialog = BottomSheetDialog(requireContext())
        val sheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_sheet_new_message, null)

        // setup recycler view for following
        val recyclerView = sheetView.findViewById<RecyclerView>(R.id.following_RV)
        val emptyText = sheetView.findViewById<TextView>(R.id.empty_Text)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // fetch following list from Firestore
        db.collection("users").document(currentUid)
            .collection("following")
            .get()
            .addOnSuccessListener { followingSnapshot ->
                val following_uid_List = followingSnapshot.documents.map { it.id }

                // if not following, show empty text
                if (following_uid_List.isEmpty()) {
                    emptyText.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE

                    return@addOnSuccessListener
                }

                // fetch following profiles from Firestore
                val followed_prof_List = mutableListOf<User>()
                var otherUIds = following_uid_List.size

                // fetch each following profile and add to list
                for (uid in following_uid_List) {
                    db.collection("users").document(uid).get()
                        .addOnSuccessListener { userDoc ->
                            val name = userDoc.getString("name") ?: uid
                            followed_prof_List.add(User(uid = uid, name = name))
                            otherUIds--
                            // Build and attach adapter to recycler view
                            if (otherUIds == 0) {
                                recyclerView.adapter =
                                    object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                                        inner class UserViewHolder(view: View) :
                                            RecyclerView.ViewHolder(view) {
                                            val nameText: TextView =
                                                view.findViewById(R.id.newM_pfpName)
                                            val sayHiBtn: MaterialButton =
                                                view.findViewById(R.id.sayHi_btn)

                                        }

                                        override fun onCreateViewHolder(
                                            parent: ViewGroup,
                                            viewType: Int
                                        ): RecyclerView.ViewHolder {
                                            // inflate the new message user layout
                                            val v = LayoutInflater.from(parent.context)
                                                .inflate(
                                                    R.layout.item_new_message_user,
                                                    parent,
                                                    false
                                                )
                                            return UserViewHolder(v)
                                        }

                                        override fun onBindViewHolder(
                                            holder: RecyclerView.ViewHolder,
                                            position: Int
                                        ) {
                                            // bind user data to view
                                            val user = followed_prof_List[position]
                                            // capitalize first letter of name
                                            (holder as UserViewHolder).nameText.text =
                                                user.name.replaceFirstChar { it.uppercase() }
                                            holder.sayHiBtn.setOnClickListener {
                                                openConversation(
                                                    dialog,
                                                    user
                                                )
                                            }
                                            holder.itemView.setOnClickListener {
                                                openConversation(
                                                    dialog,
                                                    user
                                                )
                                            }
                                        }

                                        override fun getItemCount() = followed_prof_List.size
                                    }

                                emptyText.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                            }
                        }
                        .addOnFailureListener { otherUIds-- }
                }
            }
            .addOnFailureListener { e ->
                Log.e("MessagesFragment", "Failed to load following: ${e.message}")
            }
        dialog.setContentView(sheetView)
        dialog.show()

    }


    // Dismisses the sheet and opens a dms with the user
    private fun openConversation(dialog: BottomSheetDialog, user: User) {
        dialog.dismiss()
        val intent = Intent(requireContext(), ConversationActivity::class.java)
        intent.putExtra("chat_name", user.name.replaceFirstChar { it.uppercase() })
        intent.putExtra("receiver_id", user.uid)
        startActivity(intent)
    }


    // sets up recycler views for chat
    private fun setupRecyclerViews() {
        binding.recyclerViewChats.layoutManager = LinearLayoutManager(context)
    }

    // fetches conversations from Firestore and sets up chat list
    private fun loadRalConversations() {
        // get current user UID
        val currentUid = auth.currentUser?.uid ?: return
        db.collection("conversations")
            .get(Source.SERVER)
            .addOnSuccessListener { result ->
                val chats = mutableListOf<Chat>()
                var pending = result.size()

                // Show an empty list if there are no conversations
                if (pending == 0) {
                    showChats(chats)
                    return@addOnSuccessListener
                }

                for (doc in result.documents) {
                    val conversationId = doc.id
                    // Skip conversations that don't belong to the current user
                    if (!conversationId.contains(currentUid)) {
                        pending--
                        if (pending == 0) showChats(chats)
                        continue
                    }

                    // Derive the other user's UID from the conversation ID
                    val otherUid = if (conversationId.startsWith(currentUid)) {
                        conversationId.removePrefix(currentUid + "_")
                    } else {
                        conversationId.removeSuffix("_" + currentUid)
                    }

                    // fetch last message
                    db.collection("conversations").document(conversationId)
                        .collection("messages")
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { messages ->
                            val lastMessage = messages.documents.firstOrNull()
                            val lastText = lastMessage?.getString("text") ?: ""
                            val lastTime = lastMessage?.getLong("timestamp") ?: 0L

                            // format time stamp or leave empty if no messages
                            val timeStr = if (lastTime > 0) {
                                val sdf = java.text.SimpleDateFormat(
                                    "h:mm a",
                                    java.util.Locale.getDefault()
                                )
                                sdf.format(java.util.Date(lastTime))
                            } else ""

                            // fetch other user's name
                            db.collection("users").document(otherUid).get()
                                .addOnSuccessListener { userDoc ->
                                    val name = userDoc.getString("name") ?: otherUid
                                    chats.add(
                                        Chat(
                                            name = name.replaceFirstChar { it.uppercase() },
                                            message = lastText,
                                            time = timeStr,
                                            imageResId = R.drawable.ic_person,
                                            isOnline = false,
                                            receiverId = otherUid
                                        )
                                    )
                                    pending--
                                    if (pending == 0) showChats(chats)
                                }
                                .addOnFailureListener {
                                    pending--
                                    if (pending == 0) showChats(chats)
                                }

                        }
                        .addOnFailureListener {
                            pending--
                            if(pending == 0) showChats(chats)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Messages", "SERVER ERROR: ${e.message}")
            }
    }

    // sorts and shows chats
    private fun showChats(chats: List<Chat>){
        allChats = chats.sortedByDescending { it.time }
        adapter = ChatAdapter(allChats)
        binding.recyclerViewChats.adapter = adapter
    }

    // sets up search bar to filter chats list by name
    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterChats(newText)
                return true
            }
        })
    }

    // filters chats based on search query
    private fun filterChats(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            allChats
        } else {
            allChats.filter { it.name.contains(query, ignoreCase = true) }
        }
        binding.recyclerViewChats.adapter = ChatAdapter(filteredList)
    }

    // refresh chats when returning to fragment
    override fun onResume() {
        super.onResume()
        loadRalConversations()
    }

    // nullify binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
