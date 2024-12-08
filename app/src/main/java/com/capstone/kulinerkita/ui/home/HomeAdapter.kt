package com.capstone.kulinerkita.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.NewsHome
import com.capstone.kulinerkita.data.model.Restaurant

class HomeAdapter(
    private var restaurantList: List<Restaurant>,
    private val onItemClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        val tvRestaurantName: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val tvRestaurantAddress: TextView = itemView.findViewById(R.id.tvRestaurantAddress)
        val tvCategorySuhu: TextView = itemView.findViewById(R.id.tvCategoriSuhu)
        val tvCategoryEco: TextView = itemView.findViewById(R.id.tvCategoriEco)
        val tvRating: TextView = itemView.findViewById(R.id.Tv_ratings)

        fun bind(restaurant: Restaurant) {
            // Set data ke view
            Glide.with(itemView.context)
                .load(restaurant.imageResId)
                .into(imgProfile)

            tvRestaurantName.text = restaurant.name
            tvRestaurantAddress.text = getShortenedAddress(restaurant.address)
            tvCategorySuhu.text = restaurant.categorize_weather
            tvCategoryEco.text = restaurant.eco_friendly.toString()
            tvRating.text = restaurant.rating

            // Klik item untuk detail
            itemView.setOnClickListener {
                onItemClick(restaurant)
            }
        }
    }

    // Fungsi untuk mempersingkat alamat
    private fun getShortenedAddress(address: String): String {
        // Split alamat berdasarkan koma (",")
        val parts = address.split(", ")
        return if (parts.size >= 2) "${parts[0]}, ${parts[1]}" else address
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restoran, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(restaurantList[position])
    }

    override fun getItemCount(): Int = restaurantList.size

    fun updateData(newList: List<Restaurant>) {
        restaurantList = newList
        notifyDataSetChanged()
    }
}

class NewsHomeAdapter(
    private val newsList: List<NewsHome>,
    private val onItemClick: (NewsHome) -> Unit
) : RecyclerView.Adapter<NewsHomeAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgNews: ImageView = itemView.findViewById(R.id.imgNewsThumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.Tv_newsJudul)

        fun bind(news: NewsHome) {
            // Set data ke view
            Glide.with(itemView.context)
                .load(news.imageUrl)
                .into(imgNews)

            tvTitle.text = news.title

            // Klik item untuk detail
            itemView.setOnClickListener {
                onItemClick(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_home, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

}



