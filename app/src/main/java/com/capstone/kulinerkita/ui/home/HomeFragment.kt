package com.capstone.kulinerkita.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.FragmentHomeBinding
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var restaurantList: List<Restaurant>
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Data Dummy
        restaurantList = listOf(
            Restaurant(
                name = "Warung Sate",
                address = "Jl. Kebon Jeruk, Jakarta",
                categorySuhu = "Nyaman",
                categoryEco = "Eco-Friendly",
                ratings = "2.5",
                imageResId = R.drawable.restoran_1
            ),
            Restaurant(
                name = "Bakmi Naga",
                address = "Jl. Gajah Mada, Jakarta",
                categorySuhu = "Sejuk",
                categoryEco = "Eco-Friendly",
                ratings = "4.5",
                imageResId = R.drawable.restoran_1
            ),
            Restaurant(
                name = "Soto Betawi",
                address = "Jl. Sudirman, Jakarta",
                categorySuhu = "Hangat",
                categoryEco = "Eco-Friendly",
                ratings = "3.5",
                imageResId = R.drawable.restoran_1
            ),
            Restaurant(
                name = "Gudeg Jogja",
                address = "Jl. Malioboro, Yogyakarta",
                categorySuhu = "Nyaman",
                categoryEco = "Eco-Friendly",
                ratings = "4.5",
                imageResId = R.drawable.restoran_1
            )
        )

        // Setup RecyclerView
        adapter = HomeAdapter(restaurantList) {
            // Handle item click, misalnya navigasi ke detail
            startActivity(Intent(context, DetailRestoActivity::class.java))
        }

        binding.itemRestoran.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@HomeFragment.adapter
        }

        // Event Listeners
        binding.ivNotification.setOnClickListener {
            // Handle Notification Click
        }

        binding.ivSearch.setOnClickListener {
            // Handle Search Click
        }

        Log.d("HomeFragment", "Restaurant list size: ${restaurantList.size}")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
