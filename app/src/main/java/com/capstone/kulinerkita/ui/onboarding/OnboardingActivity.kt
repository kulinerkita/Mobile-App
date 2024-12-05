package com.capstone.kulinerkita.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.databinding.ActivityOnboardingBinding
import com.capstone.kulinerkita.utils.SessionManager

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter
    private val layouts = listOf(
        R.layout.onboarding_1,
        R.layout.onboarding_2,
        R.layout.onboarding_3,
        R.layout.onboarding_4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

          val sessionManager = SessionManager(this)

        // Cek apakah onboarding sudah ditampilkan
        if (sessionManager.isOnboardingShown()) {
            navigateToLastActivity()
            return
        }

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onboardingAdapter = OnboardingAdapter(layouts)
        binding.viewPager.adapter = onboardingAdapter

        // Next button listener
        binding.nextButton.setOnClickListener {
            val current = binding.viewPager.currentItem + 1
            if (current < layouts.size) {
                binding.viewPager.currentItem = current
            } else {
                navigateToLastActivity()
            }
        }

        // Skip button listener
        binding.skipButton.setOnClickListener {
            navigateToLastActivity()
        }
    }

    private fun navigateToLastActivity() {
        // Tandai bahwa onboarding sudah ditampilkan
        val sessionManager = SessionManager(this)
        sessionManager.setOnboardingShown(true)

        startActivity(Intent(this, ActivityOnboardingLast::class.java))
        finish()
    }
}
