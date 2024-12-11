package com.capstone.kulinerkita

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NavigationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.capstone.kulinerkita.ACTION_NAVIGATION_STARTED") {
            Toast.makeText(context, "Navigasi dimulai!", Toast.LENGTH_SHORT).show()
        }
    }
}
