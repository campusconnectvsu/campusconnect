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
    private lateinit var binding: ActivityUserProfBinding
    private val viewModel: UserProfViewModel by viewModels()
    private var tar_U_Id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserProfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tar_U_Id = intent.getStringExtra("USER_ID") ?: return
        binding.returnBtn.setOnClickListener { finish() }


        binding.followBtn.setOnClickListener {
            val isFollowing = viewModel.isFollowing.value == true
            if (isFollowing) {
                show_popup()
            } else {
                viewModel.toggleFollow(tar_U_Id)
            }
        }
        binding.DmBtn.setOnClickListener {
            val intent = Intent(this, ConversationActivity::class.java)
            intent.putExtra("chat_name", viewModel.userName.value ?: "")
            intent.putExtra("receiver_id", tar_U_Id)
            startActivity(intent)
        }

        setupObservers()
        viewModel.checkInitialStatus(tar_U_Id)
    }

    private fun setupObservers() {
        viewModel.userName.observe(this) { name ->
            binding.userName.text = name
        }

        viewModel.isFollowing.observe(this) { isFollowing ->
            binding.followBtn.text =
                if (isFollowing) " Following ▾" else "Follow"
            binding.DmBtn.visibility = if (isFollowing) View.VISIBLE else View.GONE
        }

        viewModel.followersCount.observe(this) { count ->
            binding.followers.text = count.toString()
        }

        viewModel.followingCount.observe(this) { count ->
            binding.following.text = count.toString()
        }

        viewModel.isBlocked.observe(this) { isBlocked ->
            if (isBlocked) finish()
        }

    }

    private fun show_popup() {
        val sheet_log = BottomSheetDialog(this)
        val sheet_popup =
            LayoutInflater.from(this).inflate(R.layout.bottom_sheet_following_options, null)

        sheet_popup.findViewById<TextView>(R.id.sProf_UserName)?.text =
            viewModel.userName.value ?: ""

        sheet_popup.findViewById<View>(R.id.unfollow_popup).setOnClickListener {
            viewModel.toggleFollow(tar_U_Id)
            sheet_log.dismiss()
        }

        sheet_popup.findViewById<View>(R.id.popup_Block).setOnClickListener {
            viewModel.blockUser(tar_U_Id)
            sheet_log.dismiss()
        }

        sheet_popup.findViewById<View>(R.id.cancel_popup).setOnClickListener {
            sheet_log.dismiss()
        }
        sheet_log.setContentView(sheet_popup)
        sheet_log.show()
    }
}
