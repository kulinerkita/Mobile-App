package com.capstone.kulinerkita

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.databinding.ActivityOnboardingBinding

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

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onboardingAdapter = OnboardingAdapter(layouts)
        binding.viewPager.adapter = onboardingAdapter

        binding.nextButton.setOnClickListener {
            val current = binding.viewPager.currentItem + 1
            if (current < layouts.size) {
                binding.viewPager.currentItem = current
            } else {
                showOnboarding5Fragment()
            }
        }

        binding.skipButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.skipButton.setOnClickListener {
            showOnboarding5Fragment()
        }
    }

    private fun showOnboarding5Fragment() {
        binding.viewPager.visibility = View.GONE
        binding.onboarding5.visibility = View.VISIBLE

        val fragment = Onboarding5Fragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.onboarding_5, fragment)
            .commit()
    }

}
