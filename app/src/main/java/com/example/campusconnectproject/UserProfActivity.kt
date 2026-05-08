package com.example.campusconnectproject

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.campusconnectproject.databinding.ActivityUserProfBinding
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog


class UserProfActivity : AppCompatActivity() {
    // view binding for profile layout
    private lateinit var binding: ActivityUserProfBinding
    // view model for user profile data
    private val viewModel: UserProfViewModel by viewModels()

    // UID of the target user
    private var tar_U_Id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserProfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // prevent system bars from overlapping the content
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //get the target user ID
        tar_U_Id = intent.getStringExtra("USER_ID") ?: return

        // go back button
        binding.ivbackBtn.setOnClickListener { finish() }

        // shows popup if user wants to unfollow/block a profile
        binding.followUBtn.setOnClickListener {
            val isFollowing = viewModel.isFollowing.value == true
            if (isFollowing) {
                show_popup()
            } else {
                viewModel.toggleFollow(tar_U_Id)
            }
        }

        // opens message screen
        binding.DmChatBtn.setOnClickListener {
            val intent = Intent(this, ConversationActivity::class.java)
            intent.putExtra("chat_name", viewModel.userName.value ?: "")
            intent.putExtra("receiver_id", tar_U_Id)
            startActivity(intent)
        }

        setupObservers()
        viewModel.checkInitialStatus(tar_U_Id)
    }

    // observe and update UI based on view model data
    private fun setupObservers() {
        // update name changes
        viewModel.userName.observe(this) { name ->
            binding.userName.text = name
        }

        //update follow btn label
        viewModel.isFollowing.observe(this) { isFollowing ->
            binding.followUBtn.text =
                if (isFollowing) " Following ▾" else "Follow"
            binding.DmChatBtn.visibility = if (isFollowing) View.VISIBLE else View.GONE
        }

        // updates followers count
        viewModel.followersCount.observe(this) { count ->
            binding.followers.text = count.toString()
        }

        // updates following count
        viewModel.followingCount.observe(this) { count ->
            binding.following.text = count.toString()
        }

        // close activity if user is blocked
        viewModel.isBlocked.observe(this) { isBlocked ->
            if (isBlocked) finish()
        }

    }

    // shows popup when user wants to unfollow/block a profile
    private fun show_popup() {
        val sheet_log = BottomSheetDialog(this)
        val sheet_popup =
            LayoutInflater.from(this).inflate(R.layout.bottom_sheet_following_options, null)

        // set profile name in popup
        sheet_popup.findViewById<TextView>(R.id.bottom_SProf_UserName)?.text =
            viewModel.userName.value ?: ""

        // unfollow profile and close popup
        sheet_popup.findViewById<View>(R.id.unfollow_op_btn).setOnClickListener {
            viewModel.toggleFollow(tar_U_Id)
            sheet_log.dismiss()
        }

        // block profile and close popup
        sheet_popup.findViewById<View>(R.id.popup_Block_op).setOnClickListener {
            viewModel.blockUser(tar_U_Id)
            sheet_log.dismiss()
        }

        // cancel popup
        sheet_popup.findViewById<View>(R.id.cancel_popup_op).setOnClickListener {
            sheet_log.dismiss()
        }
        sheet_log.setContentView(sheet_popup)
        sheet_log.show()
    }
}
