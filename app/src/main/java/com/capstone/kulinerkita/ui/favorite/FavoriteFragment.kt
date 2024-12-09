package com.capstone.kulinerkita.ui.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.FavoriteItem
import com.capstone.kulinerkita.databinding.FragmentFavoriteBinding
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity

import org.json.JSONArray

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val favoriteItems = mutableListOf<FavoriteItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadFavoriteItems()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteItems()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(favoriteItems) { favoriteItem ->
            // Intent ke DetailRestoActivity
            val intent = Intent(requireContext(), DetailRestoActivity::class.java)
            intent.putExtra("SELECTED_RESTAURANT_ID", favoriteItem.id) // Kirim ID restoran
            startActivity(intent)
        }
        binding.recyclerViewFavorite.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoriteAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadFavoriteItems() {
        favoriteItems.clear()

        val sharedPref = requireContext().getSharedPreferences("FAVORITES", Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString("FAVORITE_LIST", "[]")
        val jsonArray = JSONArray(jsonString)

        android.util.Log.d("FavoriteFragment", "loadFavoriteItems: JSON String = $jsonString")

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val idString = jsonObject.optString("id", "0")
            val id = idString.toIntOrNull() ?: 0

            favoriteItems.add(
                FavoriteItem(
                    id = id,
                    image = jsonObject.optInt("image", R.drawable.food_1),
                    name = jsonObject.optString("name", "Nama tidak tersedia"),
                    address = jsonObject.optString("address", "Alamat tidak tersedia"),
                    rating = jsonObject.optDouble("rating", 0.0)
                )
            )
        }

        android.util.Log.d("FavoriteFragment", "loadFavoriteItems: favoriteItems = $favoriteItems")

        favoriteAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
