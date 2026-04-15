package com.example.campusconnectproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.example.campusconnectproject.databinding.ActivityUserProfBinding
import com.bumptech.glide.Glide

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserProfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfBinding
    private val viewModel: UserProfViewModel by viewModels()

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

        val userId = intent.getStringExtra("USER_ID") ?: return
        
        setupObservers()
        viewModel.checkInitialStatus(userId)

        binding.followBtn.setOnClickListener {
            if (binding.followBtn.text == getString(R.string.follow) || 
                binding.followBtn.text == getString(R.string.following_label)) {
                if (binding.followBtn.text == getString(R.string.following_label)) {
                    showFollowOptions(userId)
                } else {
                    viewModel.toggleFollow(userId)
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.userName.observe(this) { name ->
            binding.userName.text = name
        }

        viewModel.isFollowing.observe(this) { isFollowing ->
            binding.followBtn.text = if (isFollowing) getString(R.string.following_label) else getString(R.string.follow)
        }

        viewModel.followersCount.observe(this) { count ->
            binding.followers.text = getString(R.string.followers_count, count)
        }

        viewModel.followingCount.observe(this) { count ->
            binding.following.text = getString(R.string.following_count, count)
        }

        viewModel.isBlocked.observe(this) { isBlocked ->
            if (isBlocked) finish()
        }

        // Example of Glide usage (Requirement 4)
        // viewModel.profileImageUrl.observe(this) { url ->
        //     Glide.with(this).load(url).circleCrop().into(binding.profileImage)
        // }
    }

    private fun showFollowOptions(userId: String) {
        val options = arrayOf(getString(R.string.unfollow), getString(R.string.block))
        android.app.AlertDialog.Builder(this)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> viewModel.toggleFollow(userId)
                    1 -> viewModel.blockUser(userId)
                }
            }
            .show()
    }
}
