package com.capstone.kulinerkita.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.kulinerkita.API.RestaurantApiClient
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.KulinerKitaDatabase
import com.capstone.kulinerkita.data.model.NewsHome
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.FragmentHomeBinding
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity
import com.capstone.kulinerkita.ui.kategori.CategoriseActivity
import com.capstone.kulinerkita.ui.search.SearchActivity
import com.capstone.kulinerkita.utils.SessionManager
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var restaurantAdapter: HomeAdapter
    private lateinit var newsAdapter: NewsHomeAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var database: KulinerKitaDatabase
    private var restaurantList: List<Restaurant> = listOf()
    private lateinit var progressBar: ProgressBar
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d("HomeFragment", "onCreateView: Memulai inisialisasi komponen")

        sessionManager = SessionManager(requireContext())
        database = KulinerKitaDatabase.getInstance(requireContext())
        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        setupAdapters()
        setupListeners()
        loadData()

        return binding.root
    }

    private fun setupAdapters() {
        Log.d("HomeFragment", "setupAdapters: Inisialisasi adapters")
        restaurantAdapter = HomeAdapter(restaurantList.toMutableList()) { selectedRestaurant ->
            val intent = Intent(context, DetailRestoActivity::class.java)
            intent.putExtra("SELECTED_RESTAURANT_ID", selectedRestaurant.id)
            startActivity(intent)
        }

        binding.itemRestoran.layoutManager = GridLayoutManager(context, 2)
        binding.itemRestoran.adapter = restaurantAdapter

        val newsList = listOf(
            NewsHome("1", "Kuliner Tradisional Nusantara", R.drawable.news_home),
            NewsHome("2", "Kuliner Kekinian", R.drawable.news_home)
        )
        newsAdapter = NewsHomeAdapter(newsList) {}
        binding.itemKulinerNews.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.itemKulinerNews.adapter = newsAdapter
    }

    private fun setupListeners() {
        Log.d("HomeFragment", "setupListeners: Menambahkan listener pada tombol dan komponen lainnya")
        binding.ivSearch.setOnClickListener {
            // Di HomeFragment atau Activity yang mengirim data
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putParcelableArrayListExtra("restaurantList", ArrayList(restaurantList)) // Mengirim data restoran
            Log.d("HomeFragment", "Mengirim data restoran: ${restaurantList.size} item")
            startActivity(intent)

        }

        binding.foodImage.setOnClickListener {
            navigateToCategory("Makanan")
        }

        binding.drinkImage.setOnClickListener {
            navigateToCategory("Minuman")
        }
    }

    private fun navigateToCategory(category: String) {
        val intent = Intent(context, CategoriseActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val cachedRestaurants = loadRestaurantsFromCache()
            withContext(Dispatchers.Main) {
                if (cachedRestaurants.isNotEmpty()) {
                    restaurantAdapter.updateData(cachedRestaurants)
                    progressBar.visibility = View.GONE
                } else {
                    fetchRestaurantData()
                }
            }
        }
        fetchWeatherData()
        fetchUserData()
    }

    private fun fetchRestaurantData() {
        Log.d("HomeFragment", "fetchRestaurantData: Mengambil data restoran dari API")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RestaurantApiClient.RestaurantApi.getRestaurants()
                if (response.isSuccessful) {
                    val restaurants = response.body() ?: listOf()
                    Log.d("HomeFragment", "fetchRestaurantData: Data berhasil diambil, menyimpan ke cache")
                    saveRestaurantsToCache(restaurants)
                    withContext(Dispatchers.Main) {
                        restaurantList = restaurants
                        restaurantAdapter.updateData(restaurantList)
                        progressBar.visibility = View.GONE
                    }
                } else {
                    Log.e("HomeFragment", "fetchRestaurantData: Gagal mengambil data, response code ${response.code()}")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Gagal memuat data restoran!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "fetchRestaurantData: Terjadi kesalahan", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Terjadi kesalahan saat memuat data!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchWeatherData() {
        val apiKey = "8c79ecf19f5084f74baa0c841a95214f"
        val city = "Surakarta"

        viewModel.fetchWeather(city, apiKey)

        viewModel.weatherData.observe(viewLifecycleOwner) { weather ->
            _binding?.let { safeBinding ->
                if (weather != null) {
                    safeBinding.TvWeather.text = "${weather.main.temp}°C, ${weather.weather[0].description}"
                } else {
                    safeBinding.TvWeather.text = "Gagal mengambil data cuaca."
                }
            }
        }
    }

    private fun fetchUserData() {
        val token = sessionManager.getToken()
        Log.d("HomeFragment", "Token yang disimpan: $token")

        if (token != null && token.startsWith("fake_token_")) {
            val userId = token.removePrefix("fake_token_").toIntOrNull()
            if (userId != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = database.userDao().getUserById(userId)
                    withContext(Dispatchers.Main) {
                        _binding?.let { safeBinding ->
                            if (user != null) {
                                // Dynamic greeting based on time
                                val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                                    in 5..10 -> "Selamat Pagi"
                                    in 11..14 -> "Selamat Siang"
                                    in 15..18 -> "Selamat Sore"
                                    else -> "Selamat Malam"
                                }
                                safeBinding.TvNameUsers.text = "$greeting, ${user.name}!"
                            } else {
                                safeBinding.TvNameUsers.text = "Hello, Guest!"
                            }
                        }
                    }
                }
            } else {
                _binding?.TvNameUsers?.text = "Hello, Guest!"
            }
        } else {
            _binding?.TvNameUsers?.text = "Hello, Guest!"
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation(onLocationRetrieved: (Double, Double) -> Unit) {
        Log.d("HomeFragment", "fetchCurrentLocation: Mengambil lokasi pengguna")
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(requireContext())
                .lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.d(
                            "HomeFragment",
                            "fetchCurrentLocation: Lokasi ditemukan: ${location.latitude}, ${location.longitude}"
                        )
                        onLocationRetrieved(location.latitude, location.longitude)
                    } else {
                        Log.e("HomeFragment", "fetchCurrentLocation: Lokasi tidak ditemukan")
                    }
                }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            Log.d("HomeFragment", "fetchCurrentLocation: Meminta izin lokasi")
        }
    }

    private fun saveRestaurantsToCache(restaurants: List<Restaurant>) {
        val sharedPreferences = requireContext().getSharedPreferences("AppCache", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val restaurantListJson = gson.toJson(restaurants)
        editor.putString("RESTAURANT_LIST", restaurantListJson)
        editor.apply()
        Log.d("HomeFragment", "saveRestaurantsToCache: Data berhasil disimpan ke cache")
    }

    // Simpan data ke cache sebelum membuka SearchActivity
    private fun openSearchActivity() {
        saveRestaurantsToCache(restaurantList)
        val intent = Intent(requireContext(), SearchActivity::class.java)
        startActivity(intent)
    }

    // Ambil data dari cache di SearchActivity
    private fun loadRestaurantsFromCache(): List<Restaurant> {
        val sharedPreferences = requireContext().getSharedPreferences("AppCache", Context.MODE_PRIVATE)
        val gson = Gson()
        val restaurantListJson = sharedPreferences.getString("RESTAURANT_LIST", null)
        return if (restaurantListJson != null) {
            val type = object : TypeToken<List<Restaurant>>() {}.type
            gson.fromJson(restaurantListJson, type)
        } else {
            listOf()
        }
    }
}
