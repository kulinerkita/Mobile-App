package com.capstone.kulinerkita.ui.detailResto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.kulinerkita.ui.maps.MapsActivity
import com.capstone.kulinerkita.R
import com.capstone.kulinerkita.data.FavoriteDatabase
import com.capstone.kulinerkita.data.model.Categorise
import com.capstone.kulinerkita.data.model.FavoriteItem
import com.capstone.kulinerkita.data.model.Feedback
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivityDetailRestoBinding
import com.capstone.kulinerkita.ui.kategori.CategoriseAdapter
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class DetailRestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRestoBinding
    private lateinit var placesClient: PlacesClient
    private var selectedRestaurant: Restaurant? = null
    private val TAG = "DetailRestoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Activity Created")
        // Inisialisasi Google Places API
        if (!Places.isInitialized()) {
            Log.d(TAG, "Menginisialisasi Google Places API")
            Places.initialize(applicationContext, "AIzaSyC5z7pB1rtnC_cgB0ErIbi8pwk0y2b9zbY")
        }
        placesClient = Places.createClient(this)
        Log.d(TAG, "Google Places API Client diinisialisasi")

        // Ambil data kategori dari Intent
        val selectedCategorise: Categorise? = intent.getParcelableExtra("SELECTED_CATEGORISE")
        selectedCategorise?.let {
            setupRecyclerView(listOf(it))
        } ?: setupRecyclerView(getDummyCategories()) // Jika tidak ada, gunakan data dummy

        // Ambil ID restoran dari Intent dan ambil data dari cache
        val selectedRestaurantId = intent.getIntExtra("SELECTED_RESTAURANT_ID", -1)
        if (selectedRestaurantId != -1) {
            selectedRestaurant = getRestaurantFromCache(selectedRestaurantId)
            selectedRestaurant?.let { restaurant ->
                Log.d(TAG, "Restoran ditemukan: ${restaurant.name}")
                bindRestaurantData(restaurant)

                if (restaurant.placeId.isNullOrEmpty()) {
                    Log.e(TAG, "placeId tidak ditemukan untuk restoran ini")
                } else {
                    Log.d(TAG, "Mengambil ulasan untuk placeId: ${restaurant.placeId}")
                    fetchFeedbackFromGoogleMaps(restaurant.placeId)
                }
            }

        }

        // Tombol kembali
        binding.backButtonDetail.setOnClickListener { finish() }

        // Tombol cek lokasi
        binding.ButtonCekLokasi.setOnClickListener {
            selectedRestaurant?.let { restaurant ->
                val intent = Intent(this, MapsActivity::class.java).apply {
                    putExtra("LATITUDE", restaurant.latitude)
                    putExtra("LONGITUDE", restaurant.longitude)
                    putExtra("RESTAURANT_NAME", restaurant.name)
                    putExtra("RESTAURANT_ID", restaurant.id)
                    putExtra("RESTAURANT_ADDRESS", restaurant.address)
                    putExtra("RESTAURANT_ECO_FRIENDLY", restaurant.eco_friendly)
                    putExtra("CATEGORY_ID", restaurant.category_id)
                    putExtra("RESTAURANT_WEATHER", restaurant.categorize_weather)
                    putExtra("RESTAURANT_RATING", restaurant.rating)
                }
                startActivity(intent)
            } ?: Toast.makeText(this, "Lokasi tidak tersedia!", Toast.LENGTH_SHORT).show()
        }
        binding.imgFavorite.setOnClickListener {
            selectedRestaurant?.let { toggleFavorite(it) }
        }

    }

    private fun getRestaurantFromCache(restaurantId: Int): Restaurant? {
        val sharedPreferences = getSharedPreferences("AppCache", Context.MODE_PRIVATE)
        val restaurantListJson = sharedPreferences.getString("RESTAURANT_LIST", null)
        Log.d(TAG, "Restaurant List JSON: ${restaurantListJson?.take(100)}")

        return if (restaurantListJson != null) {
            val gson = com.google.gson.Gson()
            val type = object : com.google.gson.reflect.TypeToken<List<Restaurant>>() {}.type
            val restaurantList: List<Restaurant> = gson.fromJson(restaurantListJson, type)
            restaurantList.find { it.id == restaurantId }
        } else null
    }

    private fun bindRestaurantData(restaurant: Restaurant) {
        binding.tvRestaurantDetail.text = restaurant.name
        binding.tvDetailAddress.text = restaurant.address
        binding.tvPhone.text = restaurant.phone_number ?: "Tidak tersedia"
        binding.tvPriceRange.text = "Rp. ${restaurant.min_price} - Rp. ${restaurant.max_price}"
        binding.tvCategoriEcoDetail.text = if (restaurant.eco_friendly == 1) "Eco-Friendly" else "Non-Eco-Friendly"
        binding.tvCategoriSuhuDetail.text = restaurant.categorize_weather ?: "Tidak Diketahui"
        binding.tvratingsDetail.text = restaurant.rating?.let { "$it (${restaurant.reviews} reviews)" } ?: "Rating tidak tersedia"

        binding.tvOperationalHours.text = restaurant.operating_hours?.let {
            "Jam Operasional: ${it.opening_time} - ${it.closing_time}"
        } ?: "Jam Operasional tidak tersedia"

        val isFav = isFavorite(restaurant.id)
        if (isFav) {
            binding.imgFavorite.setImageResource(R.drawable.favorite_bold)
        } else {
            binding.imgFavorite.setImageResource(R.drawable.favorite_line)
        }
    }

    private fun setupRecyclerView(categoriseList: List<Categorise>) {
        val categoriseAdapter = CategoriseAdapter(categoriseList) { categorise ->
            Toast.makeText(this, "Clicked: ${categorise.namaCategorise}", Toast.LENGTH_SHORT).show()
        }
        binding.itemKulinerDetail.apply {
            layoutManager = LinearLayoutManager(this@DetailRestoActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriseAdapter
        }
    }

    private fun fetchFeedbackFromGoogleMaps(placeId: String) {
        Log.d(TAG, "fetchFeedbackFromGoogleMaps: Memulai dengan placeId = $placeId")
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME) // Tambahkan field yang valid
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val feedbackList = listOf(

                    Feedback(
                        userName = "Review Placeholder",
                        userImage = R.drawable.profile, // Placeholder image
                        feedbackTime = "Baru saja",
                        feedbackDescription = "Ulasan tidak tersedia. Silakan cek di Google Maps.",
                        rating = place.rating?.toFloat() ?: 0.0f
                    )
                )
                setupFeedbackRecyclerView(feedbackList)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Gagal mengambil ulasan: ${exception.message}")
                Toast.makeText(this, "Gagal memuat ulasan.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupFeedbackRecyclerView(feedbackList: List<Feedback>) {
        Log.d(TAG, "Setting up Feedback RecyclerView dengan ${feedbackList.size} ulasan")
        val feedbackAdapter = FeedbackAdapter(feedbackList)
        binding.itemFeedbackDetail.apply {
            layoutManager = LinearLayoutManager(this@DetailRestoActivity)
            adapter = feedbackAdapter
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

    private fun isFavorite(restaurantId: Int): Boolean {
        var isFavorite = false
        val db = FavoriteDatabase.getInstance(this)
        val favoriteDao = db.favoriteDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteItem = favoriteDao.getFavoriteById(restaurantId)
            isFavorite = favoriteItem != null

            launch(Dispatchers.Main) {
                val isFav = isFavorite
                if (isFav) {
                    binding.imgFavorite.setImageResource(R.drawable.favorite_bold)
                } else {
                    binding.imgFavorite.setImageResource(R.drawable.favorite_line)
                }
            }
        }

        return isFavorite
    }


    private fun toggleFavorite(restaurant: Restaurant) {
        val db = FavoriteDatabase.getInstance(this)
        val favoriteDao = db.favoriteDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val existingFavorite = favoriteDao.getFavoriteById(restaurant.id)

            if (existingFavorite == null) {
                // Tambahkan ke favorit
                val newFavorite = FavoriteItem(
                    id = restaurant.id,
                    name = restaurant.name,
                    address = restaurant.address,
                    rating = restaurant.rating,
                    image = R.drawable.restoran_1
                )
                favoriteDao.addFavorite(newFavorite)

                runOnUiThread {
                    val isFav = isFavorite(restaurant.id)
                    if (isFav) {
                        binding.imgFavorite.setImageResource(R.drawable.favorite_bold)
                    } else {
                        binding.imgFavorite.setImageResource(R.drawable.favorite_line)
                    }
                    Toast.makeText(this@DetailRestoActivity,
                        if (isFav) "Ditambahkan ke Favorit" else "Dihapus dari Favorit",
                        Toast.LENGTH_SHORT).show()
                }

            } else {
                // Hapus dari favorit
                favoriteDao.removeFavorite(existingFavorite)

                runOnUiThread {
                    Toast.makeText(this@DetailRestoActivity, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}