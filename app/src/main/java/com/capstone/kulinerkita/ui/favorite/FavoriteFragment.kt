package com.capstone.kulinerkita.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.FavoriteDatabase
import com.capstone.kulinerkita.data.model.FavoriteItem
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoriteItems = mutableListOf<FavoriteItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter = FavoriteAdapter(favoriteItems) { item ->
            showItemDetails(item)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewFavorite)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favoriteAdapter

        loadFavorites()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadFavorites() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = FavoriteDatabase.getInstance(requireContext())
                val favoriteDao = db.favoriteDao()
                val favorites = favoriteDao.getAllFavorites()

                withContext(Dispatchers.Main) {
                    favoriteItems.clear()
                    favoriteItems.addAll(favorites)
                    favoriteAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Error loading favorites", e)
            }
        }
    }


    private fun showItemDetails(item: FavoriteItem) {
        // Membuka halaman DetailRestoActivity dan meneruskan data item favorit
        val intent = Intent(requireContext(), DetailRestoActivity::class.java).apply {
            putExtra("SELECTED_RESTAURANT_ID", item.id)
            putExtra("SELECTED_RESTAURANT_NAME", item.name)
            putExtra("SELECTED_RESTAURANT_ADDRESS", item.address)
            putExtra("SELECTED_RESTAURANT_RATING", item.rating)
        }
        startActivity(intent)
    }

}
