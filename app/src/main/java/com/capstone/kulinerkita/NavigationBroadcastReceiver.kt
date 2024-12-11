package com.capstone.kulinerkita

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.capstone.kulinerkita.ui.maps.MapsNavigationActivity

class NavigationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.capstone.kulinerkita.ACTION_NAVIGATION_STARTED") {
            Toast.makeText(context, "Navigasi dimulai!", Toast.LENGTH_SHORT).show()

            // Pastikan context bukan null dan cast ke Context non-null
            context?.let {
                // Panggil MapsNavigationActivity untuk menjalankan animasi
                val animationIntent = Intent(it, MapsNavigationActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                it.startActivity(animationIntent)
            }
        }
    }
}
