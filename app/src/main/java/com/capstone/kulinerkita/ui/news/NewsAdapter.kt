package com.capstone.kulinerkita.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.NewsItem
import com.capstone.kulinerkita.databinding.ItemNewsBinding

class NewsAdapter(private val newsList: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemNewsBinding.bind(view)
        fun bind(news: NewsItem) {
            binding.tvNewsTitle.text = news.title
            binding.tvNewsDescription.text = news.description
            binding.imgNewsThumbnail.setImageResource(news.thumbnail)
            binding.tvNewsTimestamp.text = news.timestamp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size
}
