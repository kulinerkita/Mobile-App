package com.capstone.kulinerkita

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

// NotificationReceiver.kt
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val response = intent.getStringExtra("response")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Menghapus notifikasi setelah pengguna menekan tombol
        notificationManager.cancel(1)

        if (response == "yes") {
            // Menangani aksi jika user setuju
            Toast.makeText(context, "Maps Enabled", Toast.LENGTH_SHORT).show()
        } else {
            // Menangani aksi jika user tidak setuju
            Toast.makeText(context, "Maps not enabled", Toast.LENGTH_SHORT).show()
        }
    }
}

