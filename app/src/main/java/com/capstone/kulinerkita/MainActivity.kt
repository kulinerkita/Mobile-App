package com.capstone.kulinerkita

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.kulinerkita.databinding.ActivityMain2Binding
import com.capstone.kulinerkita.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menonaktifkan ActionBar jika tidak digunakan
        supportActionBar?.hide()

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil username dari Intent
        val username = intent.getStringExtra("username")

        // Kirim username ke HomeFragment jika ada
        if (username != null) {
            val homeFragment = HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("username", username)
                }
            }

            // Menambahkan HomeFragment ke fragment_container
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main2, homeFragment)
                .commit()
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main2)

        AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorite,
                R.id.navigation_news,
                R.id.navigation_profile
            )
        )

        navView.setupWithNavController(navController)

        // Navigasi langsung ke HomeFragment saat aplikasi dibuka
        if (username == null) {
            navController.navigate(R.id.navigation_home)
        }
    }
}
