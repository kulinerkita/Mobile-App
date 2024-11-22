package com.capstone.kulinerkita.ui.notification

data class Notification(
    val iconResId: Int,
    val title: String,
    val message: String,
    val timeAgo: String,
    val actionText: String? = null
)
