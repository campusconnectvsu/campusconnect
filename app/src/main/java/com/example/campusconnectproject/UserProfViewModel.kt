package com.example.campusconnectproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class UserProfViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val curUserId = FirebaseAuth.getInstance().currentUser?.uid

    // Listener registrations to be cleared in onCleared()
    private var userListener: ListenerRegistration? = null
    private var followingStatusListener: ListenerRegistration? = null
    private var followersCountListener: ListenerRegistration? = null
    private var followingCountListener: ListenerRegistration? = null
    private var blockListener: ListenerRegistration? = null

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _isFollowing = MutableLiveData<Boolean>()
    val isFollowing: LiveData<Boolean> = _isFollowing

    private val _followersCount = MutableLiveData<Long>()
    val followersCount: LiveData<Long> = _followersCount

    private val _followingCount = MutableLiveData<Long>()
    val followingCount: LiveData<Long> = _followingCount

    private val _isBlocked = MutableLiveData<Boolean>()
    val isBlocked: LiveData<Boolean> = _isBlocked

    /**
     * Starts real-time listeners for all profile data.
     */
    fun checkInitialStatus(targetId: String) {
        val current = curUserId ?: return

        // 1. Listen to if the current user is blocked by the target
        blockListener = db.collection("users").document(targetId)
            .collection("blocked_users").document(current)
            .addSnapshotListener { snapshot, _ ->
                _isBlocked.value = snapshot?.exists() == true
            }

        // 2. Listen to Profile Data (Username)
        userListener = db.collection("users").document(targetId)
            .addSnapshotListener { snapshot, _ ->
                _userName.value = snapshot?.getString("name") ?: "Unknown"
            }

        // 3. Listen to Following Status
        followingStatusListener = db.collection("users").document(current)
            .collection("following").document(targetId)
            .addSnapshotListener { snapshot, _ ->
                _isFollowing.value = snapshot?.exists() == true
            }

        // 4. Real-time Followers Count
        followersCountListener = db.collection("users").document(targetId)
            .collection("followers")
            .addSnapshotListener { snapshot, _ ->
                _followersCount.value = snapshot?.size()?.toLong() ?: 0L
            }

        // 5. Real-time Following Count
        followingCountListener = db.collection("users").document(targetId)
            .collection("following")
            .addSnapshotListener { snapshot, _ ->
                _followingCount.value = snapshot?.size()?.toLong() ?: 0L
            }
    }

    fun toggleFollow(targetId: String) {
        val current = curUserId ?: return
        val isCurrentlyFollowing = _isFollowing.value ?: false
        val batch = db.batch()

        val followingRef = db.collection("users").document(current).collection("following").document(targetId)
        val followersRef = db.collection("users").document(targetId).collection("followers").document(current)

        if (isCurrentlyFollowing) {
            batch.delete(followingRef)
            batch.delete(followersRef)
        } else {
            batch.set(followingRef, hashMapOf("followed" to true))
            batch.set(followersRef, hashMapOf("followed" to true))
        }

        // The listeners will handle updating the UI once the batch is committed
        batch.commit()
    }

    fun blockUser(targetId: String) {
        val current = curUserId ?: return
        val batch = db.batch()

        val blockRef = db.collection("users").document(current).collection("blocked_users").document(targetId)
        batch.set(blockRef, hashMapOf("blocked" to true))

        val followingRef = db.collection("users").document(current).collection("following").document(targetId)
        batch.delete(followingRef)

        val followersRef = db.collection("users").document(targetId).collection("followers").document(current)
        batch.delete(followersRef)

        batch.commit()
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up listeners to prevent memory leaks and unnecessary data usage
        userListener?.remove()
        followingStatusListener?.remove()
        followersCountListener?.remove()
        followingCountListener?.remove()
        blockListener?.remove()
    }
}
