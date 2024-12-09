package com.capstone.kulinerkita.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.FavoriteItem
import com.capstone.kulinerkita.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val favoriteItems: List<FavoriteItem>,
    private val onItemClick: (FavoriteItem) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        private val tvName: TextView = itemView.findViewById(R.id.tvRestaurantName)
        private val tvAddress: TextView = itemView.findViewById(R.id.tvRestaurantAddress)
        private val tvRating: TextView = itemView.findViewById(R.id.Tv_ratings)

        fun bind(item: FavoriteItem) {
            imgProfile.setImageResource(item.image)
            tvName.text = item.name
            tvAddress.text = item.address
            tvRating.text = item.rating.toString()

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restoran, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteItems[position])
    }

    override fun getItemCount(): Int = favoriteItems.size
}
