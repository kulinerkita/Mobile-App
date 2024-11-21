package com.capstone.kulinerkita.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.ui.login.SignInActivity
import com.capstone.kulinerkita.ui.register.CreateAccountActivity

class ActivityOnboardingLast : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_last)

        findViewById<Button>(R.id.signInButton).setOnClickListener {
            // Arahkan ke halaman login
            startActivity(Intent(this, SignInActivity::class.java))
        }

        findViewById<Button>(R.id.signUpButton).setOnClickListener {
            // Arahkan ke halaman registrasi
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }
}