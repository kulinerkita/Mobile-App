package com.capstone.kulinerkita.ui.kategori

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.API.RestaurantApiClient
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivityCategoriesKulinerBinding
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity
import com.capstone.kulinerkita.ui.home.HomeAdapter
import com.capstone.kulinerkita.ui.home.HomeViewModel
import kotlinx.coroutines.launch

class CategoriesKulinerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesKulinerBinding
    private lateinit var restaurantAdapter: HomeAdapter
    private val viewModel: HomeViewModel by viewModels()

    private var categoryId: Int = -1 // Nilai default untuk categoryId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoriesKulinerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan categoryId dari Intent
        categoryId = intent.getIntExtra("CATEGORY_ID", -1)

        // Memanggil API untuk mendapatkan restoran berdasarkan categoryId
        if (categoryId != -1) {
            fetchRestaurants(categoryId)
        }

        // Ambil data cuaca
        fetchWeatherData()

        binding.backButtonKuliner.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fetchWeatherData() {
        val apiKey = "8c79ecf19f5084f74baa0c841a95214f"
        val city = "Surakarta"

        // Memanggil fetchWeather() untuk memulai request ke API cuaca
        viewModel.fetchWeather(city, apiKey)

        lifecycleScope.launch {
            // Mengamati LiveData untuk mendapatkan data cuaca
            viewModel.weatherData.observe(this@CategoriesKulinerActivity) { response ->
                if (response != null) {
                    val temperature = response.main.temp.toInt()
                    val weatherDescription = response.weather[0].description

                    // Tentukan kategori cuaca berdasarkan suhu
                    val weatherCategory = getWeatherCategory(temperature)
                    // Panggil API restoran dengan kategori cuaca
                    fetchRestaurants(categoryId, weatherCategory)

                    Log.d("CategoriesKuliner", "Cuaca: $weatherCategory, Suhu: $temperature°C, $weatherDescription")
                } else {
                    Toast.makeText(this@CategoriesKulinerActivity, "Gagal mengambil data cuaca.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getWeatherCategory(temperature: Int): String {
        return when {
            temperature in 20..28 -> "Dingin"
            temperature in 29..35 -> "Panas"
            else -> "Panas"
        }
    }

    private fun fetchRestaurants(categoryId: Int, weatherCategory: String? = null) {
        val apiService = RestaurantApiClient.RestaurantApi

        binding.progressKuliner.visibility = android.view.View.VISIBLE

        lifecycleScope.launch {
            try {
                // Ambil restoran berdasarkan categoryId dan kategori cuaca (jika ada)
                val response = apiService.getRestaurantsByCategoryAndWeather(categoryId, weatherCategory)

                if (response.isSuccessful) {
                    val restaurants = response.body() ?: emptyList()
                    setupRecyclerView(restaurants)
                } else {
                    Toast.makeText(this@CategoriesKulinerActivity, "Gagal mengambil restoran: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e("CategoriesKuliner", "Gagal mengambil restoran: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@CategoriesKulinerActivity, "Error mengambil restoran: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            } finally {
                // Sembunyikan progress bar setelah data diproses
                binding.progressKuliner.visibility = android.view.View.GONE
            }
        }
    }

    private fun setupRecyclerView(restaurants: List<Restaurant>) {
        restaurantAdapter = HomeAdapter(restaurants) { restaurant ->
            val intent = Intent(this, DetailRestoActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restaurant.id)
            startActivity(intent)
        }

        binding.itemRestoran.apply {
            layoutManager = GridLayoutManager(this@CategoriesKulinerActivity, 2)
            adapter = restaurantAdapter
        }
    }
}



