package com.example.campusconnectproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfActivity : AppCompatActivity() {
    // UI variables
    private lateinit var uName: TextView
    private lateinit var followBtn: Button
    private lateinit var followersC: TextView
    private lateinit var followingC: TextView

    // Firebase variables
    private val db = FirebaseFirestore.getInstance()
    private val curUser = FirebaseAuth.getInstance().currentUser?.uid
    private var user_ID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_prof)

        // bind UI variables to layout
        uName = findViewById(R.id.userName)
        followBtn = findViewById(R.id.followBtn)
        followingC = findViewById(R.id.following)
        followersC = findViewById(R.id.followers)

        // get user id from intent
        user_ID = intent.getStringExtra("USER_ID")
        UData()
        ifFollowing()
        followCounts()
        ifBlocked()

        // set up click listener for follow button
        followBtn.setOnClickListener {
            if (followBtn.text == "Follow") {
                follow()
            } else {
                followOptions()
            }
        }

    }


    // check if profile is blocked by current user
    private fun ifBlocked() {
        val current = curUser ?: return
        val target = user_ID ?: return

        // check if user is blocked
        db.collection("users")
            .document(target)
            .collection("blocked_users")
            .document(current)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    finish()
                } else {
                    UData()
                    ifFollowing()
                    followCounts()
                }
            }
    }

    //show options for unfollowing or blocking a user
    private fun followOptions() {
        val options = arrayOf("Unfollow", "block")

        val builder = android.app.AlertDialog.Builder(this)
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> unfollow()
                1 -> blockprof()
            }
        }
        builder.show()
    }


    // load user data from firestore
    private fun UData() {
        user_ID?.let { uid ->
            // load user name from firestore
            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val name = doc.getString("name")
                    uName.text = name
                }

        }

    }

    // follow profile
    private fun follow() {
        val currentUser = curUser ?: return
        val target = user_ID ?: return
        // add user to following collection
        db.collection("users")
            .document(currentUser)
            .collection("following")
            .document(target)
            .set(hashMapOf("followed" to true))

        // add target to followers collection
        db.collection("users")
            .document(target)
            .collection("followers")
            .document(currentUser)
            .set(hashMapOf("followed" to true))
        followBtn.text = "Following"
        followCounts()

    }

    // check if user is already following profile
    private fun ifFollowing() {
        val current = curUser ?: return
        val target = user_ID ?: return
        db.collection("users")
            .document(current)
            .collection("following")
            .document(target)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    followBtn.text = "Following"
                } else {
                    followBtn.text = "Follow"
                }
            }
    }

    // unfollow profile
    private fun unfollow() {
        val current = curUser ?: return
        val target = user_ID ?: return

        // remove profile from following
        db.collection("users")
            .document(current)
            .collection("following")
            .document(target)
            .delete()


        // remove profile from followers
        db.collection("users")
            .document(target)
            .collection("followers")
            .document(current)
            .delete()

        followBtn.text = "Follow"
        followCounts()
    }

    // block profile
    private fun blockprof() {
        val current = curUser ?: return
        val target = user_ID ?: return

        // add a profile to blocked users
        db.collection("users")
            .document(current)
            .collection("blocked_users")
            .document(target)
            .set(hashMapOf("blocked" to true))

        // remove a profile from following
        db.collection("users")
            .document(current)
            .collection("following")
            .document(target)
            .delete()

        // remove a profile from followers
        db.collection("users")
            .document(target)
            .collection("followers")
            .document(current)
            .delete()

        followBtn.text = "Blocked"
        followBtn.isEnabled = false
    }


    // update follow counts
    private fun followCounts() {
        val target = user_ID ?: return

        // get follow counts
        db.collection("users")
            .document(target)
            .collection("followers")
            .get()
            .addOnSuccessListener { result ->
                followersC.text = result.size().toString()
            }
        db.collection("users")
            .document(target)
            .collection("following")
            .get()
            .addOnSuccessListener { result ->
                followingC.text = result.size().toString()
            }
    }
}



