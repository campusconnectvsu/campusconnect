package com.example.campusconnectproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class UserProfViewModel : ViewModel() {
    // firestore db instance and auth instance
    private val db = FirebaseFirestore.getInstance()
    private val curUserId = FirebaseAuth.getInstance().currentUser?.uid

    // Listener registrations to be cleared in onCleared()
    private var userListener: ListenerRegistration? = null
    private var followingStatusListener: ListenerRegistration? = null
    private var followersCountListener: ListenerRegistration? = null
    private var followingCountListener: ListenerRegistration? = null
    private var blockListener: ListenerRegistration? = null

    // display name of the user
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    // show if user is following the profile
    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> = _isFollowing

    // number of followers user follows
    private val _followersCount = MutableLiveData<Long>()
    val followersCount: LiveData<Long> = _followersCount

    // number of profiles that the user is following
    private val _followingCount = MutableLiveData<Long>()
    val followingCount: LiveData<Long> = _followingCount

    // shows if user as blocked the profile
    private val _isBlocked = MutableLiveData<Boolean>()
    val isBlocked: LiveData<Boolean> = _isBlocked


    // starts real-time listeners for all profile data.
    fun checkInitialStatus(targetId: String) {
        val current = curUserId ?: return

        // listen if user is blocked
        blockListener = db.collection("users").document(targetId)
            .collection("blocked_users").document(current)
            .addSnapshotListener { snapshot, _ ->
                _isBlocked.value = snapshot?.exists() == true
            }

        // Listen for target Username change
        userListener = db.collection("users").document(targetId)
            .addSnapshotListener { snapshot, _ ->
                _userName.value = snapshot?.getString("name") ?: "Unknown"
            }

        // shows if user is following the profile
        followingStatusListener = db.collection("users").document(current)
            .collection("following").document(targetId)
            .addSnapshotListener { snapshot, _ ->
                _isFollowing.value = snapshot?.exists() == true
            }

        //  listen for followers count changes
        followersCountListener = db.collection("users").document(targetId)
            .collection("followers")
            .addSnapshotListener { snapshot, _ ->
                _followersCount.value = snapshot?.size()?.toLong() ?: 0L
            }

        // listen for following count changes
        followingCountListener = db.collection("users").document(targetId)
            .collection("following")
            .addSnapshotListener { snapshot, _ ->
                _followingCount.value = snapshot?.size()?.toLong() ?: 0L
            }
    }

    // toggle follow/ unfollow button
    fun toggleFollow(targetId: String) {
        val current = curUserId ?: return
        val isCurrentlyFollowing = _isFollowing.value ?: false
        val batch = db.batch()

        // Reference to follow and unfollow
        val followingRef =
            db.collection("users").document(current).collection("following").document(targetId)
        val followersRef =
            db.collection("users").document(targetId).collection("followers").document(current)

        if (isCurrentlyFollowing) {
            // Unfollow the user
            batch.delete(followingRef)
            batch.delete(followersRef)
            batch.commit()
        } else {
            // Follow the user and send a notification
            batch.set(followingRef, hashMapOf("followed" to true))
            batch.set(followersRef, hashMapOf("followed" to true))
            batch.commit().addOnSuccessListener {
                db.collection("users").document(current).get()
                    .addOnSuccessListener { doc ->
                        val sender_userName = doc.getString("name")
                            ?.replaceFirstChar { it.uppercase() } ?: "Someone"
                        NotificationsActivity.sendNotification(
                            db = db,
                            targetUid = targetId,
                            title = "$sender_userName started following you",
                            body = "Tap to view their profile",
                            type = "follow"
                        )
                    }
            }
        }

    }

    // block the profile and removes all references
    fun blockUser(targetId: String) {
        val current = curUserId ?: return
        val batch = db.batch()

        // add profile to blocked list
        val blockRef =
            db.collection("users").document(current).collection("blocked_users").document(targetId)
        batch.set(blockRef, hashMapOf("blocked" to true))

        // remove the profile from following list
        val followingRef =
            db.collection("users").document(current).collection("following").document(targetId)
        batch.delete(followingRef)

        // remove the profile from followers list
        val followersRef =
            db.collection("users").document(targetId).collection("followers").document(current)
        batch.delete(followersRef)

        batch.commit()
    }

    // removes all active listeners
    override fun onCleared() {
        super.onCleared()
        userListener?.remove()
        followingStatusListener?.remove()
        followersCountListener?.remove()
        followingCountListener?.remove()
        blockListener?.remove()
    }
}
