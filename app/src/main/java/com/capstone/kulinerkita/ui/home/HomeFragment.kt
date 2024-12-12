package com.capstone.kulinerkita.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
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
import android.provider.Settings
import com.capstone.kulinerkita.API.MLApiClient
import com.capstone.kulinerkita.ui.notification.NotificationActivity
import com.google.android.gms.location.FusedLocationProviderClient

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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
            intent.putExtra("SELECTED_RESTAURANT_ID", selectedRestaurant)
            startActivity(intent)
        }

        binding.itemRestoran.layoutManager = GridLayoutManager(context, 2)
        binding.itemRestoran.adapter = restaurantAdapter

        val newsList = listOf(
            NewsHome(
                id = "1",
                title = "Makanan Khas Surakarta",
                imageRes = R.drawable.news_home1,
                url = "https://budayanesia.com/makanan-khas-surakarta/"
            ),
            NewsHome(
                id = "2",
                title = "Lenjongan Pasar Gede, Kuliner Legendaris Surakarta",
                imageRes = R.drawable.news_home2,
                url = "https://kumparan.com/seputar-solo/lenjongan-pasar-gede-kuliner-legendaris-surakarta-23uwpW71gKo"
            ),
            NewsHome(
                id = "3",
                title = "Menjelajahi Potensi Pemanfaatan Sisa Pangan Berlebih Dari Pasar Jebres di Kota Surakarta",
                imageRes = R.drawable.news_home3,
                url = "https://aliansizerowaste.id/2024/10/27/menjelajahi-potensi-pemanfaatan-sisa-pangan-berlebih-dari-pasar-jebres-di-kota-surakarta/"
            )
        )

        newsAdapter = NewsHomeAdapter(newsList) { url ->
            openUrl(url)
        }
        binding.itemKulinerNews.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.itemKulinerNews.adapter = newsAdapter
    }
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
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

        binding.ivNotification.setOnClickListener{
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToCategory(category: String) {
        val intent = Intent(context, CategoriseActivity::class.java)
        intent.putExtra("CATEGORY", category)
        startActivity(intent)
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE
        // Hapus pemanggilan loadRestaurantsFromCache
        fetchRestaurantData() // Langsung ambil data dari API
        fetchWeatherData()
        fetchUserData()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            // If permission is not granted, request it
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, fetch location
            checkAndFetchLocation()
        }
    }

    // Function to check if location services are enabled and fetch location
    private fun checkAndFetchLocation() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isLocationEnabled) {
            fetchCurrentLocation()
        } else {
            // Prompt user to enable location services
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent, LOCATION_SETTINGS_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val address = addresses?.get(0)

                val city = address?.locality ?: "Unknown City"

                binding.TvLocation.text = "$city"
            } else {
                Toast.makeText(requireContext(), "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handle the permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, check if location services are enabled and fetch location
                checkAndFetchLocation()
            } else {
                // Permission denied, show a message
                Toast.makeText(requireContext(), "Location permission is required to fetch your location.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handle result of enabling location services
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            checkAndFetchLocation()  // Re-check if location is enabled after user changes setting
        }
    }

    override fun onStart() {
        super.onStart()
        // Request location permission when fragment is started
        requestLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun fetchRestaurantData() {
        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Get the current location
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                Log.d("HomeFragment", "Kirim lokasi: Latitude: $latitude, Longitude: $longitude")

                val locationMap = mapOf("latitude" to latitude, "longitude" to longitude)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // Use the correct method `getRecommendedRestaurants`
                        val response = MLApiClient.retrofitService.getRecommendedRestaurants(locationMap)

                        if (response.isSuccessful) {
                            val restaurantResponse = response.body()
                            Log.d("HomeFragment", "Respons API: ${response.body()}")
                            val recommendedRestaurants = restaurantResponse?.data ?: emptyList()
                            Log.d("HomeFragment", "Data restoran diterima: ${recommendedRestaurants.size} restoran ditemukan")
                            withContext(Dispatchers.Main) {
                                if (recommendedRestaurants.isNotEmpty()) {
                                    restaurantList = recommendedRestaurants
                                    restaurantAdapter.updateData(restaurantList)
                                } else {
                                    Log.d("HomeFragment", "Tidak ada restoran yang ditemukan")
                                }
                                progressBar.visibility = View.GONE
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Tidak ada restoran yang ditemukan di area ini", Toast.LENGTH_SHORT).show()
                                progressBar.visibility = View.GONE
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error fetching restaurant data", e)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Terjadi kesalahan saat memuat data!", Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            } else {
                Log.d("HomeFragment", "Lokasi saat ini tidak tersedia")
                Toast.makeText(requireContext(), "Lokasi tidak ditemukan", Toast.LENGTH_SHORT).show()
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
                    safeBinding.TvWeather.text = "${weather.main.temp}°C"
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val LOCATION_SETTINGS_REQUEST_CODE = 2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify binding untuk menghindari memory leaks
    }
}