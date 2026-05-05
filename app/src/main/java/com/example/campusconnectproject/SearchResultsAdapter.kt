package com.example.campusconnectproject

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class SearchUser(
    val uid: String,
    val name: String,
    var followCount: Int = 0,
    var following: Boolean = false
)

class SearchResultsAdapter(
    private var users: List<SearchUser>
) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val current_Uid = FirebaseAuth.getInstance().currentUser?.uid

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar: ShapeableImageView = view.findViewById(R.id.pfp_Av_pic)
        val prof_UName: TextView = view.findViewById(R.id.dis_username)
        val prof_followerCount: TextView = view.findViewById(R.id.followers_C)
        val follow_btn: MaterialButton = view.findViewById(R.id.follow_bn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        val context = holder.itemView.context

        holder.prof_UName.text = user.name.replaceFirstChar { it.uppercase() }

        val countLabel = if (user.followCount == 1) "1 follower" else "${user.followCount} followers"
        holder.prof_followerCount.text = countLabel

        change_followBtn(holder.follow_btn, user.following, context)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, UserProfActivity:: class.java)
            intent.putExtra("USER_ID", user.uid)
            context.startActivity(intent)
        }

        holder.follow_btn.setOnClickListener {
            val cur = current_Uid ?: return@setOnClickListener
            val tar_Id = user.uid
            val batch = db.batch()

            val following_Ref = db.collection("users").document(cur)
                .collection("following").document(tar_Id)
            val  followers_Ref = db.collection("users").document(tar_Id)
                .collection("followers").document(cur)

            if (user. following){
                batch.delete(following_Ref)
                batch.delete(followers_Ref)
                user.following = false
                user.followCount = maxOf(0, user.followCount - 1 )
                batch.commit()
            } else{
                batch.set(following_Ref, hashMapOf("followed" to true))
                batch.set(followers_Ref, hashMapOf("followed" to true))
                user.following = true
                user.followCount += 1
                batch.commit().addOnSuccessListener {
                    db.collection("users").document(cur).get()
                        .addOnSuccessListener { doc ->
                            val myName = doc.getString("name")
                                ?.replaceFirstChar { it.uppercase() }?: "Someone"
                            NotificationsActivity.sendNotification(
                                db = db,
                                targetUid = tar_Id,
                                title = "$myName started following you",
                                body = "Tap to view their profile",
                                type = "follow"
                            )
                        }
                }
            }

            batch.commit()
            notifyItemChanged(position)
        }
    }


    private fun change_followBtn(btn: MaterialButton, isFollowing: Boolean, context: android.content.Context){
        if( isFollowing){
            btn.text = "Following"
            btn.backgroundTintList = ColorStateList.valueOf(context.getColor(android.R.color.transparent))
            btn.setTextColor(context.getColor(R.color.primary_blue))
            btn.strokeWidth = 2
        } else {
            btn.text = "Follow"
            btn.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.primary_blue))
            btn.setTextColor(context.getColor(android.R.color.white))
            btn.strokeWidth = 0
        }
    }

    override  fun getItemCount () = users.size
    fun update_prof(newUsers: List<SearchUser>){
        users = newUsers
        notifyDataSetChanged()
    }
}