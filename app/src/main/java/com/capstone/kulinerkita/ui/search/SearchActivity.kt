package com.capstone.kulinerkita.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.API.RestaurantApiService
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivitySearchBinding
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity
import com.capstone.kulinerkita.ui.home.HomeAdapter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var restaurantAdapter: HomeAdapter
    private var restaurantList: List<Restaurant> = listOf()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://kulinerkita.et.r.appspot.com/") // Ganti dengan URL API yang sesuai
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(RestaurantApiService::class.java)

    companion object {
        private const val TAG = "SearchActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView dan adapter
        setupRecyclerView()

        // Ambil data restoran dari API
        getRestaurants()

        // Tambahkan event listener untuk input pencarian
        binding.etSearch.addTextChangedListener {
            filterRestaurant(it.toString())
        }
    }

    private fun getRestaurants() {
        // Menampilkan ProgressBar saat mengambil data
        binding.progressBar.visibility = View.VISIBLE

        // Mengambil data restoran dari API
        lifecycleScope.launch {
            try {
                val response: Response<List<Restaurant>> = apiService.getRestaurants()
                if (response.isSuccessful) {
                    restaurantList = response.body() ?: listOf()
                    Log.d(TAG, "Data restoran diterima: ${restaurantList.size} item")

                    // Perbarui adapter dengan data baru
                    restaurantAdapter.updateData(restaurantList)

                    // Sembunyikan ProgressBar setelah data diterima
                    binding.progressBar.visibility = View.GONE
                } else {
                    Log.e(TAG, "Gagal mengambil data: ${response.code()}")
                    binding.progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e(TAG, "Terjadi kesalahan saat mengambil data", e)
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
        // Setup adapter untuk RecyclerView
        restaurantAdapter = HomeAdapter(restaurantList.toMutableList()) { restaurant ->
            Log.d(TAG, "Restoran dipilih: ${restaurant.name}")

            // Navigasi ke DetailActivity dan kirim data restoran
            val intent = Intent(this, DetailRestoActivity::class.java)
            intent.putExtra("SELECTED_RESTAURANT", restaurant)
            startActivity(intent)
        }

        binding.rvSearchResults.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            adapter = restaurantAdapter
        }
    }

    private fun filterRestaurant(query: String) {
        val filteredList = restaurantList.filter {
            it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
        }

        if (filteredList.isEmpty()) {
            binding.tvNoResults.visibility = View.VISIBLE
        } else {
            binding.tvNoResults.visibility = View.GONE
        }

        restaurantAdapter.updateData(filteredList)
    }
}
