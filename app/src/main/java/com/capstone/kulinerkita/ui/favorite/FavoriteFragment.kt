package com.capstone.kulinerkita.ui.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.data.model.FavoriteItem
import com.capstone.kulinerkita.databinding.FragmentFavoriteBinding

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

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(favoriteItems)
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

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            favoriteItems.add(
                FavoriteItem(
                    id = jsonObject.getInt("id"),
                    image = jsonObject.getInt("image"),
                    name = jsonObject.getString("name"),
                    address = jsonObject.getString("address"),
                    rating = jsonObject.getDouble("rating")
                )
            )
        }

        favoriteAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
