package com.capstone.kulinerkita.ui.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notifications = listOf(
            Notification(
                R.drawable.reminder,
                getString(R.string.reminder_title),
                getString(R.string.reminder_message),
                getString(R.string.days_ago)
            ),
            Notification(
                R.drawable.culinary_event,
                getString(R.string.culinary_event_title),
                getString(R.string.culinary_event_message),
                getString(R.string.days_ago)
            ),
            Notification(
                R.drawable.journey_reminder,
                getString(R.string.culinary_journey_title),
                getString(R.string.culinary_journey_message),
                getString(R.string.days_ago),
                getString(R.string.activate_reminder)
            )
        )

        val adapter = NotificationAdapter(notifications)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            this.adapter = adapter
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}