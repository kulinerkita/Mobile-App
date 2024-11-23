package com.capstone.kulinerkita.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.data.model.FavoriteItem
import com.capstone.kulinerkita.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val favoriteList: List<FavoriteItem>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteItem) {
            binding.imgProfile.setImageResource(item.image)
            binding.tvRestaurantName.text = item.name
            binding.tvRestaurantAddress.text = item.address
            binding.TvRatings.text = item.rating.toString()

            // Concatenate categories
            binding.tvCategoriSuhu.text = item.categories.joinToString(", ")

            // Click listener for favorite button
            binding.imgFavorite.setOnClickListener {
                // Handle favorite icon click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }

    override fun getItemCount(): Int = favoriteList.size
}
