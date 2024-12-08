package com.capstone.kulinerkita.ui.detailResto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.kulinerkita.MapsActivity
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.model.Categorise
import com.capstone.kulinerkita.data.model.Feedback
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivityDetailRestoBinding
import com.capstone.kulinerkita.ui.kategori.CategoriseAdapter

@Suppress("DEPRECATION")
class DetailRestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRestoBinding
    private var selectedRestaurant: Restaurant? = null
    private val TAG = "DetailRestoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Activity Created")

        // Ambil data dari intent
        val selectedCategorise: Categorise? = intent.getParcelableExtra("SELECTED_CATEGORISE")
        if (selectedCategorise == null) {
            Log.e(TAG, "Categorise tidak diterima! Menggunakan data dummy.")
            setupRecyclerView(getDummyCategories()) // Gunakan data dummy
        } else {
            Log.d(TAG, "Categorise diterima: ${selectedCategorise.namaCategorise}")
            setupRecyclerView(listOf(selectedCategorise))
        }

        // Memanggil fungsi untuk menampilkan feedback
        setupFeedbackRecyclerView(getDummyFeedback())

        // Ambil ID restoran jika tersedia
        val selectedRestaurantId = intent.getIntExtra("SELECTED_RESTAURANT_ID", -1)
        if (selectedRestaurantId != -1) {
            selectedRestaurant = getRestaurantFromCache(selectedRestaurantId)
            if (selectedRestaurant != null) {
                Log.d(TAG, "Selected Restaurant: ${selectedRestaurant!!.name}")
                bindRestaurantData(selectedRestaurant!!)
            } else {
                Log.e(TAG, "Restaurant not found in cache for ID: $selectedRestaurantId")
                Toast.makeText(this, "Restoran tidak ditemukan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Tombol kembali
        binding.backButtonDetail.setOnClickListener {
            finish()
        }

        // Tombol cek lokasi
        binding.ButtonCekLokasi.setOnClickListener {
            val restaurant = selectedRestaurant
            if (restaurant != null) {
                Log.d(TAG, "Navigating to MapsActivity with Lat: ${restaurant.latitude}, Lng: ${restaurant.longitude}")
                val intent = Intent(this, MapsActivity::class.java).apply {
                    putExtra("LATITUDE", restaurant.latitude)
                    putExtra("LONGITUDE", restaurant.longitude)
                }
                startActivity(intent)
            } else {
                Log.e(TAG, "SelectedRestaurant is null when trying to navigate to Maps")
                Toast.makeText(this, "Data lokasi tidak tersedia!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRestaurantFromCache(restaurantId: Int): Restaurant? {
        val sharedPreferences = getSharedPreferences("AppCache", Context.MODE_PRIVATE)
        val restaurantListJson = sharedPreferences.getString("RESTAURANT_LIST", null)
        Log.d(TAG, "Restaurant List JSON: ${restaurantListJson?.substring(0, 100) ?: "null"}")

        return if (restaurantListJson != null) {
            val gson = com.google.gson.Gson()
            val type = object : com.google.gson.reflect.TypeToken<List<Restaurant>>() {}.type
            val restaurantList: List<Restaurant> = gson.fromJson(restaurantListJson, type)
            Log.d(TAG, "Total Restaurants in Cache: ${restaurantList.size}")

            val restaurant = restaurantList.find { it.id == restaurantId }
            if (restaurant != null) {
                Log.d(TAG, "Restaurant Found: ${restaurant.name}")
            } else {
                Log.e(TAG, "No Restaurant matches the ID: $restaurantId")
            }
            restaurant
        } else {
            Log.e(TAG, "Restaurant List JSON is null")
            null
        }
    }

    private fun bindRestaurantData(restaurant: Restaurant) {
        Log.d(TAG, "Binding data for Restaurant: ${restaurant.name}")
        binding.tvRestaurantDetail.text = restaurant.name
        binding.tvDetailAddress.text = restaurant.address
        binding.tvPhone.text = restaurant.phone_number ?: "Tidak tersedia"
        binding.tvPriceRange.text = "Rp. ${restaurant.min_price} - Rp. ${restaurant.max_price}"
        binding.tvCategoriEcoDetail.text =
            if (restaurant.eco_friendly == 1) "Eco-Friendly" else "Non-Eco-Friendly"
        binding.tvCategoriSuhuDetail.text = restaurant.categorize_weather ?: "Tidak Diketahui"
        binding.tvratingsDetail.text =
            restaurant.rating?.let { "$it (${restaurant.reviews} reviews)" } ?: "Rating tidak tersedia"
    }

    private fun setupRecyclerView(categoriseList: List<Categorise>) {
        Log.d(TAG, "Setting up RecyclerView with ${categoriseList.size} items")

        val categoriseAdapter = CategoriseAdapter(categoriseList) { categorise ->
            Toast.makeText(this, "Clicked: ${categorise.namaCategorise}", Toast.LENGTH_SHORT).show()
        }

        binding.itemKulinerDetail.apply {
            layoutManager = LinearLayoutManager(this@DetailRestoActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriseAdapter
        }
    }

    private fun getDummyCategories(): List<Categorise> {
        return listOf(
            Categorise(1, "Wedang Uwuh", "Minuman", R.drawable.wedang_uwuh),
            Categorise(2, "Timlo", "Makanan", R.drawable.timlo),
            Categorise(3, "Tahok", "Makanan", R.drawable.tahok),
            Categorise(4, "Soto", "Makanan", R.drawable.soto),
            Categorise(5, "Serabi", "Makanan", R.drawable.serabi),
            Categorise(6, "Selat", "Makanan", R.drawable.selat),
            Categorise(7, "Sego Liwet", "Makanan", R.drawable.sego_liwet),
            Categorise(8, "Sate Kere", "Makanan", R.drawable.sate_kere),
            Categorise(9, "Sate Buntel", "Makanan", R.drawable.sate_buntel),
            Categorise(10, "Lenjongan", "Makanan", R.drawable.lenjongan),
            Categorise(11, "Gudeg", "Makanan", R.drawable.gudeg),
            Categorise(12, "Gendar Pecel", "Makanan", R.drawable.gendar_pecel),
            Categorise(13, "Wedang Asle", "Minuman", R.drawable.wedang_asle),
            Categorise(14, "Es Kapal", "Minuman", R.drawable.es_kapal),
            Categorise(15, "Es Gempol Pleret", "Minuman", R.drawable.es_gempol_pleret),
            Categorise(16, "Es Dawet Telasih", "Minuman", R.drawable.es_dawet_telasih),
            Categorise(17, "Brambang Asem", "Makanan", R.drawable.brambang_asem)
        )
    }

    private fun setupFeedbackRecyclerView(feedbackList: List<Feedback>) {
        val feedbackAdapter = FeedbackAdapter(feedbackList)
        binding.itemFeedbackDetail.apply {
            layoutManager = LinearLayoutManager(this@DetailRestoActivity)
            adapter = feedbackAdapter
        }
    }

    private fun getDummyFeedback(): List<Feedback> {
        return listOf(
            Feedback(
                userName = "John Doe",
                userImage = R.drawable.food_1, // Gambar pengguna (contoh)
                feedbackTime = "2 days ago",
                feedbackDescription = "Makanannya enak, pelayanannya cepat, suasana nyaman!",
                rating = 4.5f
            ),
            Feedback(
                userName = "Jane Smith",
                userImage = R.drawable.food_1, // Gambar pengguna (contoh)
                feedbackTime = "1 week ago",
                feedbackDescription = "Soto yang disajikan sangat autentik, akan kembali lagi!",
                rating = 5.0f
            ),
            Feedback(
                userName = "Mark Johnson",
                userImage = R.drawable.food_1, // Gambar pengguna (contoh)
                feedbackTime = "3 days ago",
                feedbackDescription = "Cukup baik, tetapi perlu perbaikan di area kebersihan.",
                rating = 3.0f
            )
        )
    }

}
