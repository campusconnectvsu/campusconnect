package com.example.campusconnectproject


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.campusconnectproject.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

    // profile variables
    private var prof_binding: FragmentProfileBinding? = null
    private val binding get() = prof_binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        prof_binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // load profile data
        loadProfileData()


        // set up click listeners for editing profile
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        // set up click listeners for changing interests
        binding.changeInterests.setOnClickListener {
            Toast.makeText(requireContext(), "Change interests clicked", Toast.LENGTH_SHORT)
                .show()
        }


    }

    // clean up binding
    override fun onDestroyView() {
        super.onDestroyView()
        prof_binding = null
    }

    // reload profile data
    override fun onResume() {
        super.onResume()
        loadProfileData()
    }

    private fun loadProfileData() {
        // get user id
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) return

        // load profile data from shared preferences
        val pref = requireContext().getSharedPreferences(
            "profile_data_$uid",
            android.content.Context.MODE_PRIVATE
        )

        // load edited name
        val editName = pref.getString("custom_name", null)
        if (!editName.isNullOrBlank()) {
            binding.profName.text = editName
        } else {
            val cU = FirebaseAuth.getInstance().currentUser
            cU?.email?.let { userEmail ->
                binding.profName.text = userEmail.substringBefore("@")
            }

        }


        // load profile pic
        val pic_uriString = pref.getString("profile_pic", null)
        if (!pic_uriString.isNullOrBlank()) {
            val uri = Uri.parse(pic_uriString)
            binding.profpic.setImageURI(uri)
        }

        // load about me
        val aboutMe = pref.getString("about_me", "")
        binding.aboutMeDes.text = aboutMe ?: ""

        // load interests
        val interests = pref.getString("interests", "")
        binding.interestC.removeAllViews()


        // interest chips logic
        if (!interests.isNullOrBlank()) {
            // split interests into array by commas
            interests.split(",").forEach { interests ->
                val chip = TextView(requireContext())
                chip.text = interests.trim()
                chip.setBackgroundResource(R.drawable.interest_chip)
                chip.setPadding(40, 16, 40, 16)
                val params = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(12, 12, 12, 12)
                chip.layoutParams = params
                binding.interestC.addView(chip)
            }

        }
    }


}



