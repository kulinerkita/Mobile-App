package com.capstone.kulinerkita.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.R

class NotificationAdapter(private val notifications: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val title: TextView = view.findViewById(R.id.title)
        val message: TextView = view.findViewById(R.id.message)
        val timeAgo: TextView = view.findViewById(R.id.timeAgo)
        val actionText: TextView = view.findViewById(R.id.actionText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.icon.setImageResource(notification.iconResId)
        holder.title.text = notification.title
        holder.message.text = notification.message
        holder.timeAgo.text = notification.timeAgo
        if (notification.actionText != null) {
            holder.actionText.text = notification.actionText
            holder.actionText.visibility = View.VISIBLE
        } else {
            holder.actionText.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = notifications.size
}