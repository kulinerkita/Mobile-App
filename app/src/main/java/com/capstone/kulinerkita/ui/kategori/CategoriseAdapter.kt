package com.capstone.kulinerkita.ui.kategori

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.Categorise

class CategoriseAdapter(
    private val categoriseList: List<Categorise>,
    private val onItemClick: (Categorise) -> Unit
) : RecyclerView.Adapter<CategoriseAdapter.CategoriseViewHolder>() {

    inner class CategoriseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgKuliner: ImageView = itemView.findViewById(R.id.imgKuliner)
        private val tvKulinerName: TextView = itemView.findViewById(R.id.tvKulinerName)
        private val tvCategori: TextView = itemView.findViewById(R.id.tvCategori)

        fun bind(categorise: Categorise) {
            imgKuliner.setImageResource(categorise.gamblerCategorise)
            tvKulinerName.text = categorise.namaCategorise
            tvCategori.text = categorise.typeCategorise

            // Set click listener
            itemView.setOnClickListener {
                onItemClick(categorise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kuliner, parent, false)
        return CategoriseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriseViewHolder, position: Int) {
        holder.bind(categoriseList[position])
    }

    override fun getItemCount(): Int = categoriseList.size
}
