package com.example.campusconnectproject


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Back Button
        view.findViewById<ImageView>(R.id.bArrow).setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Helper to set text of each row
        fun setRowLabel(rowId: Int, text: String) {
            val row = view.findViewById<LinearLayout>(rowId)
            val title = row.findViewById<TextView>(R.id.settingTitle)
            title.text = text
        }

        // ACCOUNT
        setRowLabel(R.id.ro_edit_prof, "Edit Profile")
        setRowLabel(R.id.ro_change_passwrd, "Change Password")
        setRowLabel(R.id.ro_delete_acc, "Delete Account")

        // NOTIFICATIONS
        setRowLabel(R.id.ro_push_notifi, "Push Notifications")

        // MAP PRIVACY
        setRowLabel(R.id.ro_Gmode, "Enable Ghost Mode")


        // GENERAL
        setRowLabel(R.id.ro_about_app, "About App")
        setRowLabel(R.id.ro_terms_conditions, "Terms and Conditions")

        // LOGOUT
        setRowLabel(R.id.ro_logout, "Logout")

        // Click listeners
        view.findViewById<LinearLayout>(R.id.ro_change_passwrd).setOnClickListener {
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }



        view.findViewById<LinearLayout>(R.id.ro_delete_acc).setOnClickListener {
            startActivity(Intent(requireActivity(), DeleteAccountActivity::class.java))
        }


        view.findViewById<LinearLayout>(R.id.ro_push_notifi).setOnClickListener {
            startActivity(Intent(requireActivity(), PushNotificationsActivity::class.java))
        }



        view.findViewById<LinearLayout>(R.id.ro_Gmode).setOnClickListener {
            startActivity(Intent(requireActivity(), GhostModeActivity::class.java))
        }



        view.findViewById<LinearLayout>(R.id.ro_about_app).setOnClickListener {
            startActivity(Intent(requireActivity(), AboutAppActivity::class.java))
        }


        view.findViewById<LinearLayout>(R.id.ro_terms_conditions).setOnClickListener {
            startActivity(Intent(requireActivity(), TermsConditionsActivity::class.java))
        }



        view.findViewById<LinearLayout>(R.id.ro_logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), com.example.campusconnectproject.loginsignup.LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }
    }
}
