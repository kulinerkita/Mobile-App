package com.capstone.kulinerkita.ui.detailResto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.Feedback

class FeedbackAdapter(private val feedbackList: List<Feedback>) :
    RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val feedbackTime: TextView = itemView.findViewById(R.id.feedbackTime)
        val feedbackDescription: TextView = itemView.findViewById(R.id.feedbackDescription)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = feedbackList[position]
        holder.userImage.setImageResource(feedback.userImage)
        holder.userName.text = feedback.userName
        holder.feedbackTime.text = feedback.feedbackTime
        holder.feedbackDescription.text = feedback.feedbackDescription
        holder.ratingBar.rating = feedback.rating
    }

    override fun getItemCount(): Int = feedbackList.size
}
