package com.capstone.kulinerkita.ui.home

import android.annotation.SuppressLint
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
            if (restaurantList.isEmpty()) {
                Log.e("HomeFragment", "Cannot send Intent, restaurantList is empty!")
                Toast.makeText(context, "Data restoran belum tersedia. Silakan tunggu!", Toast.LENGTH_SHORT).show()
                return@HomeAdapter
            }

            val intent = Intent(context, DetailRestoActivity::class.java)
            intent.putParcelableArrayListExtra("RESTAURANT_LIST", ArrayList(restaurantList))
            intent.putExtra("SELECTED_RESTAURANT_ID", selectedRestaurant.id.toString())

            Log.d("HomeFragment", "Sending Restaurant List: ${restaurantList.size}")
            Log.d("HomeFragment", "Sending Selected ID: ${selectedRestaurant.id}")
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

                    // Pastikan data restoran berhasil diterima
                    Log.d("HomeFragment", "Fetched Restaurants: ${restaurants.size}")
                    if (restaurants.isEmpty()) {
                        Log.e("HomeFragment", "Data restoran kosong!")
                    }
                    withContext(Dispatchers.Main) {
                        // Perbarui data ke variabel `restaurantList`
                        restaurantList = restaurants
                        restaurantAdapter.updateData(restaurantList)

                        // Log untuk memastikan data telah diperbarui
                        Log.d("HomeFragment", "Restaurant list updated: ${restaurantList.size}")
                        progressBar.visibility = View.GONE
                    }


                } else {
                    withContext(Dispatchers.Main) {
                        // Jika response tidak berhasil, sembunyikan progress bar
                        progressBar.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching restaurant data", e)
                withContext(Dispatchers.Main) {
                    // Jika terjadi error saat fetch data, sembunyikan progress bar
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify binding untuk menghindari memory leaks
    }
}

