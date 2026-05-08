package com.example.campusconnectproject


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DeleteAccountActivity : AppCompatActivity() {


    // input fields for user password
    private lateinit var del_Passwrd: EditText

    // btn to delete account
    private lateinit var del_Ac: Button
    // firebase auth instance
    private lateinit var fire_auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        // Initialize Firebase Auth instance and bind the views to the layout
        fire_auth = FirebaseAuth.getInstance()
        del_Passwrd = findViewById(R.id.delPasswrd)
        del_Ac = findViewById(R.id.deleteAcB)

        del_Ac.setOnClickListener {
            val password = del_Passwrd.text.toString().trim()

            // check if user entered a password
            if (password.isEmpty()) {
                show("enter your password to confirm")
                return@setOnClickListener
            }

            // confirm if you want to delete the account
            AlertDialog.Builder(this).setTitle("Delete Account")
                .setMessage("Are you sure you want to permanently delete your account?")
                .setPositiveButton("Delete") { _, _ ->
                    delete_acc()
                }.setNegativeButton("Cancel", null).show()
        }
    }

    // delete account function
    private fun delete_acc() {
        // get the currently signed in user
        val user = fire_auth.currentUser
        if (user != null) {
            user.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        show("Account deleted successfully")
                        finish()
                    } else {
                        show("Failed to delete account")
                    }
                }

        } else {
            show("User not found")
        }

    }

    private fun show(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
