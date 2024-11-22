package com.capstone.kulinerkita.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.ui.login.SignInActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val settingsMenu = findViewById<LinearLayout>(R.id.settingsMenu)
        settingsMenu.setOnClickListener {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, SettingsActivity::class.java))
        }

        val helpMenu = findViewById<LinearLayout>(R.id.helpMenu)
        helpMenu.setOnClickListener {
            Toast.makeText(this, "Help clicked", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, HelpActivity::class.java))
        }

        val logoutMenu = findViewById<LinearLayout>(R.id.logoutMenu)
        logoutMenu.setOnClickListener {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            //startActivity(Intent(this, SignInActivity::class.java))
        }

        val userName = findViewById<TextView>(R.id.userName)
        val userEmail = findViewById<TextView>(R.id.userEmail)

        userName.text = "Jhon Doe"
        userEmail.text = "example@gmail.com"
    }
}