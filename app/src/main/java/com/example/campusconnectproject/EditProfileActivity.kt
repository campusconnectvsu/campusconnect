package com.example.campusconnectproject

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.selects.select

class EditProfileActivity : AppCompatActivity() {

    // edit profile variables
    private lateinit var eUserName: EditText
    private lateinit var eEmail: EditText
    private lateinit var eAboutMe: EditText
    private lateinit var eInterests: EditText
    private lateinit var saveProfB: TextView
    private lateinit var changePicB: TextView
    private lateinit var editProfPic: ImageView
    private var pick_a_pic: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // bind the views to the layout
        eUserName = findViewById(R.id.editUName)
        eEmail = findViewById(R.id.editUEmail)
        eAboutMe = findViewById(R.id.editUAboutMe)
        eInterests = findViewById(R.id.editUInterests)
        saveProfB = findViewById(R.id.saveProfileBtn)
        changePicB = findViewById(R.id.changePicB)
        editProfPic = findViewById(R.id.editProf_pic)


        // set up a click listener for the save button
        saveProfB.setOnClickListener {
            // get the data from the edit text
            val about = eAboutMe.text.toString().trim()
            val interestsInput = eInterests.text.toString().trim()
            val nameInput = eUserName.text.toString().trim()


            // check if the user is logged in
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // save the data of user to firebase
            val preference = getSharedPreferences("profile_data_$uid", MODE_PRIVATE)
            val editor = preference.edit()

            editor.putString("about_me", about)
            editor.putString("interests", interestsInput)
            editor.putString("custom_name", nameInput)

            // edit profile pic
            pick_a_pic?.let {
                editor.putString("profile_pic", it.toString())
            }

            editor.apply()

            Toast.makeText(this,"Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // change profile pic
        changePicB.setOnClickListener {
            imagePicker.launch("image/*")
        }
    }
    // get image from photos
    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        if(uri != null ){
            pick_a_pic = uri
            editProfPic.setImageURI(uri)
        }
    }
}
