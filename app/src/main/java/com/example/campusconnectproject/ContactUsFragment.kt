package com.example.campusconnectproject


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.view.View

class ContactUsFragment : Fragment(R.layout.fragment_contact_us) {

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Set up click listeners to open the phone
        view.findViewById<TextView>(R.id.phoneNum).setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:8045245000")
            startActivity(intent)
        }

        // Set up click listeners to open the email
        view.findViewById<TextView>(R.id.email).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:CampConnect@vsu.edu")
            startActivity(intent)
        }

        // Set up click listeners to open the school website
        view.findViewById<Button>(R.id.moreInfoButton).setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("linkto:www.vsu.edu")
            startActivity(intent)
        }
    }
}
