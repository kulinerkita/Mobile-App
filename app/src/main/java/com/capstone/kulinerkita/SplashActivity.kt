package com.capstone.kulinerkita

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.ui.onboarding.OnboardingActivity
import com.capstone.kulinerkita.utils.SessionManager
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        titleTextView.startAnimation(slideUpAnimation)

        sessionManager = SessionManager(this)

        GlobalScope.launch {
            delay(2000)
            if (sessionManager.isLoggedIn()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
            }
            finish()
        }
    }
}
