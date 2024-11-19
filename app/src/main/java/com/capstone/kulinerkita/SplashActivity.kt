package com.capstone.kulinerkita

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)

        titleTextView.startAnimation(slideUpAnimation)

        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
            finish()
        }
    }
}
