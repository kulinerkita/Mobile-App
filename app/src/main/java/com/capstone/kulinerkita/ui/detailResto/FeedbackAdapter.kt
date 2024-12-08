package com.capstone.kulinerkita.ui.detailResto

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.Feedback
import com.capstone.kulinerkita.databinding.ItemFeedbackBinding

class FeedbackAdapter(private val feedbackList: List<Feedback>) :
    RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    inner class FeedbackViewHolder(private val binding: ItemFeedbackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(feedback: Feedback) {
            binding.TvNameUsers.text = feedback.userName
            binding.imgUser.setImageResource(feedback.userImage)
            binding.TvWaktuFeedback.text = feedback.feedbackTime
            binding.TvDeskripsiFeedback.text = feedback.feedbackDescription
            binding.ratingBar.rating = feedback.rating
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val binding = ItemFeedbackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeedbackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        holder.bind(feedbackList[position])
    }

    override fun getItemCount(): Int = feedbackList.size
}
