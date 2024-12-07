package com.capstone.kulinerkita.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
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
import com.capstone.kulinerkita.utils.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var restaurantAdapter: HomeAdapter
    private lateinit var newsAdapter: NewsHomeAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var database: KulinerKitaDatabase
    private var restaurantList: List<Restaurant> = listOf()
    private lateinit var progressBar: ProgressBar
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Fetch user data from FirebaseAuth
        val username = arguments?.getString("username") ?: "Guest"
        binding.TvNameUsers.text = "Welcome, $username!"

        sessionManager = SessionManager(requireContext())
        database = KulinerKitaDatabase.getInstance(requireContext())
        progressBar = binding.progressBar

        fetchWeatherData()
        fetchUserData()

        progressBar.visibility = View.VISIBLE

        // Data Dummy untuk berita
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
        restaurantAdapter = HomeAdapter(restaurantList.toMutableList()) { selectedRestaurant ->
            // Kirim ID restoran ke DetailRestoActivity
            val intent = Intent(context, DetailRestoActivity::class.java)
            intent.putExtra("SELECTED_RESTAURANT_ID", selectedRestaurant.id)
            startActivity(intent)
        }

        fetchRestaurantData()

        binding.itemRestoran.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = restaurantAdapter
        }

        newsAdapter = NewsHomeAdapter(newsList) {
            // Handle klik berita jika diperlukan
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

        binding.foodImage.setOnClickListener {
            val intent = Intent(context, CategoriseActivity::class.java)
            intent.putExtra("CATEGORY", "Makanan")
            startActivity(intent)
        }

        binding.drinkImage.setOnClickListener {
            val intent = Intent(context, CategoriseActivity::class.java)
            intent.putExtra("CATEGORY", "Minuman")
            startActivity(intent)
        }

        Log.d("HomeFragment", "Restaurant list size: ${restaurantList.size}")
        Log.d("HomeFragment", "News Di Home list size: ${newsList.size}")

        return binding.root
    }

    @SuppressLint("SetTextI18n")
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
                                safeBinding.TvNameUsers.text = "${user.name}!"
                            } else {
                                safeBinding.TvNameUsers.text = "Guest!"
                            }
                        }
                    }
                }
            } else {
                _binding?.TvNameUsers?.text = "Guest!"
            }
        } else {
            _binding?.TvNameUsers?.text = "Guest!"
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

    private fun fetchRestaurantData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RestaurantApiClient.RestaurantApi.getRestaurants()
                if (response.isSuccessful) {
                    val restaurants = response.body() ?: listOf()

                    // Simpan data ke SharedPreferences
                    saveRestaurantsToCache(restaurants)

                    withContext(Dispatchers.Main) {
                        restaurantList = restaurants
                        restaurantAdapter.updateData(restaurantList)

                        progressBar.visibility = View.GONE
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Gagal memuat data restoran!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching restaurant data", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Terjadi kesalahan!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveRestaurantsToCache(restaurants: List<Restaurant>) {
        val sharedPreferences = requireContext().getSharedPreferences("AppCache", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Konversi daftar restoran ke JSON
        val gson = Gson()
        val restaurantListJson = gson.toJson(restaurants)

        // Simpan ke SharedPreferences
        editor.putString("RESTAURANT_LIST", restaurantListJson)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify binding untuk menghindari memory leaks
    }
}