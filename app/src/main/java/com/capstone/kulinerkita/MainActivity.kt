package com.capstone.kulinerkita

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, Onboarding5Fragment())
                .commit()
        }
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment != null) {
            Log.d("MainActivity", "Fragment saat ini: ${currentFragment::class.java.simpleName}")
        } else {
            Log.d("MainActivity", "Tidak ada fragment dalam container fragmentContainer")
        }

    }

}
