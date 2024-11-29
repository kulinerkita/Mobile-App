package com.capstone.kulinerkita.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.NewsHome
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.FragmentHomeBinding
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var restaurantAdapter: HomeAdapter
    private lateinit var newsAdapter: NewsHomeAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        fetchWeatherData()

        // Data Dummy
        val restaurantList = listOf(
            Restaurant(
                name = "Warung Sate",
                address = "Jl. Kebon Jeruk, Jakarta",
                categorySuhu = "Nyaman",
                categoryEco = "Eco-Friendly",
                ratings = "2.5",
                imageResId = R.drawable.restoran_1,
                operationalHours = mapOf(
                    "Senin" to "08:00 - 12:00",
                    "Selasa" to "08:00 - 12:00",
                    "Rabu" to "08:00 - 12:00",
                    "Kamis" to "08:00 - 12:00",
                    "Jumat" to "08:00 - 12:00",
                    "Sabtu" to "08:00 - 12:00",
                    "Minggu" to "08:00 - 12:00"
                )
            ),
            Restaurant(
                name = "Bakmi Naga",
                address = "Jl. Gajah Mada, Jakarta",
                categorySuhu = "Sejuk",
                categoryEco = "Eco-Friendly",
                ratings = "4.5",
                imageResId = R.drawable.restoran_1,
                operationalHours = mapOf(
                    "Senin" to "08:00 - 12:00",
                    "Selasa" to "08:00 - 12:00",
                    "Rabu" to "08:00 - 12:00",
                    "Kamis" to "08:00 - 12:00",
                    "Jumat" to "08:00 - 12:00",
                    "Sabtu" to "08:00 - 12:00",
                    "Minggu" to "08:00 - 12:00"
                )
            ),
            Restaurant(
                name = "Soto Betawi",
                address = "Jl. Sudirman, Jakarta",
                categorySuhu = "Hangat",
                categoryEco = "Eco-Friendly",
                ratings = "3.5",
                imageResId = R.drawable.restoran_1,
                operationalHours = mapOf(
                    "Senin" to "08:00 - 12:00",
                    "Selasa" to "08:00 - 12:00",
                    "Rabu" to "08:00 - 12:00",
                    "Kamis" to "08:00 - 12:00",
                    "Jumat" to "08:00 - 12:00",
                    "Sabtu" to "08:00 - 12:00",
                    "Minggu" to "08:00 - 12:00"
                )
            ),
            Restaurant(
                name = "Gudeg Jogja",
                address = "Jl. Malioboro, Yogyakarta",
                categorySuhu = "Nyaman",
                categoryEco = "Eco-Friendly",
                ratings = "4.5",
                imageResId = R.drawable.restoran_1,
                operationalHours = mapOf(
                    "Senin" to "08:00 - 12:00",
                    "Selasa" to "08:00 - 12:00",
                    "Rabu" to "08:00 - 12:00",
                    "Kamis" to "08:00 - 12:00",
                    "Jumat" to "08:00 - 12:00",
                    "Sabtu" to "08:00 - 12:00",
                    "Minggu" to "08:00 - 12:00"
                )
            )
        )

        // data dummy news
        val newsList = listOf(
            NewsHome(
                id = "1",
                title = "Kuliner Tradisional Nusantara",
                imageUrl = R.drawable.news_home
            ),
            NewsHome(
                id = "2",
                title = "Kuliner Tradisional Nusantara",
                imageUrl = R.drawable.news_home
            )
        )


        // Setup RecyclerView
        restaurantAdapter = HomeAdapter(restaurantList) {
            // Handle restaurant item click
            startActivity(Intent(context, DetailRestoActivity::class.java))
        }

        newsAdapter = NewsHomeAdapter(newsList) {

        }

        binding.itemKulinerNews.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = newsAdapter
        }

        binding.itemRestoran.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = restaurantAdapter
        }

        // Event Listeners
        binding.ivNotification.setOnClickListener {
            // Handle Notification Click
        }

        binding.ivSearch.setOnClickListener {
            // Handle Search Click
        }

        Log.d("HomeFragment", "Restaurant list size: ${restaurantList.size}")
        Log.d("HomeFragment", "News Di Home list size: ${newsList.size}")

        return binding.root
    }

    private fun fetchWeatherData() {
        val apiKey = "8c79ecf19f5084f74baa0c841a95214f"
        val city = "Surakarta"

        viewModel.fetchWeather(city, apiKey)

        viewModel.weatherData.observe(viewLifecycleOwner) { weather ->
            if (weather != null) {
                binding.TvWeather.text = "${weather.main.temp}°C, ${weather.weather[0].description}"
            } else {
                binding.TvWeather.text = "Gagal mengambil data cuaca."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
