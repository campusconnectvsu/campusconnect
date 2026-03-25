package com.example.campusconnectproject


import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {

    // current/new/confirm password
    private lateinit var password: EditText
    private lateinit var newPassword: EditText
    private lateinit var confPassword: EditText
    private lateinit var saveBtn: TextView

    // firebase auth
    private lateinit var userdata: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        userdata = FirebaseAuth.getInstance()


        // link the views
        password = findViewById(R.id.cur_Password)
        newPassword = findViewById(R.id.new_Password)
        confPassword = findViewById(R.id.con_Password)
        saveBtn = findViewById(R.id.savePassBtn)

        saveBtn.setOnClickListener {
            val curPassword = password.text.toString().trim()
            val newPassword = this@ChangePasswordActivity.newPassword.text.toString().trim()
            val conPassword = confPassword.text.toString().trim()

            // change password logic
            when {
                curPassword.isEmpty() -> {
                    show("Enter your  password")
                }
                newPassword.isEmpty() -> {
                    show("Enter a new password")
                }
                conPassword.isEmpty() -> {
                    show("Confirm your new password")
                }
                newPassword != conPassword -> {
                    show(" password do not match")
                }
                else -> {
                    // update password to firebase
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.updatePassword(newPassword)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                show("Password changed successfully")
                            } else {
                                show("Password change failed")
                            }
                        }
                }
            }
        }
    }

    private fun show(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

