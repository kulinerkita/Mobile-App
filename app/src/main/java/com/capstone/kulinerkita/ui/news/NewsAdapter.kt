package com.capstone.kulinerkita.ui.news

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.data.model.Article
import com.capstone.kulinerkita.databinding.ItemNewsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(private val newsList: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemNewsBinding.bind(view)

        fun bind(article: Article) {
            // Tampilkan judul, deskripsi, gambar, dan waktu
            binding.tvNewsTitle.text = article.title
            binding.tvNewsDescription.text = article.description
            binding.imgNewsThumbnail.setImageResource(article.img)

            // Format waktu menjadi "dd MMM yyyy"
            val formattedTime = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(article.time)
            binding.tvNewsTimestamp.text = formattedTime

            // Arahkan ke URL saat item di klik
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(article.url)
                }
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            com.capstone.kulinerkita.R.layout.item_news,
            parent,
            false
        )
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size
}
