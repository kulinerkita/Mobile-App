package com.capstone.kulinerkita.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.FavoriteItem
import com.capstone.kulinerkita.databinding.FragmentFavoriteBinding

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data for favorites
        favoriteItems.addAll(
            listOf(
                FavoriteItem(
                    image = R.drawable.restoran_1,
                    name = "Restoran A",
                    address = "Jl. ABC No. 1",
                    rating = 4.5,
                    categories = listOf("Nyaman")
                ),
                FavoriteItem(
                    image = R.drawable.restoran_1,
                    name = "Restoran B",
                    address = "Jl. XYZ No. 2",
                    rating = 4.2,
                    categories = listOf("Dingin")
                )
            )
        )

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter(favoriteItems)
        binding.recyclerViewFavorite.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = favoriteAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}