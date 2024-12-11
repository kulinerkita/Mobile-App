package com.capstone.kulinerkita.ui.detailResto

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

@Suppress("DEPRECATION")
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
            Places.initialize(applicationContext, "AIzaSyC5z7pB1rtnC_cgB0ErIbi8pwk0y2b9zbY")
        }
        placesClient = Places.createClient(this)

        // Ambil data kategori dari Intent
        val selectedCategorise: Categorise? = intent.getParcelableExtra("SELECTED_CATEGORISE")
        selectedCategorise?.let {
            setupRecyclerView(listOf(it))
        } ?: setupRecyclerView(getDummyCategories()) // Jika tidak ada, gunakan data dummy


        // Ambil data restoran dari Intent
        val restaurant = intent.getParcelableExtra<Restaurant>("SELECTED_RESTAURANT_ID")
        if (restaurant != null) {
            selectedRestaurant = restaurant
            bindRestaurantData(restaurant)
        } else {
            Toast.makeText(this, "Restoran tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
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

    private fun bindRestaurantData(restaurant: Restaurant) {
        binding.tvRestaurantDetail.text = restaurant.name ?: "Nama tidak tersedia"
        binding.tvDetailAddress.text = restaurant.address ?: "Alamat tidak tersedia"
        binding.tvPhone.text = restaurant.phone_number ?: "Nomor telepon tidak tersedia"
        binding.tvPriceRange.text = if (restaurant.min_price != null && restaurant.max_price != null) {
            "Rp. ${restaurant.min_price} - Rp. ${restaurant.max_price}"
        } else {
            "Harga tidak tersedia"
        }
        binding.tvCategoriEcoDetail.text = if (restaurant.eco_friendly == 1) "Eco-Friendly" else "Non-Eco-Friendly"
        binding.tvCategoriSuhuDetail.text = restaurant.categorize_weather ?: "Informasi cuaca tidak tersedia"
        binding.tvratingsDetail.text = restaurant.rating?.let { "$it (${restaurant.reviews ?: 0} reviews)" } ?: "Rating tidak tersedia"
        binding.tvOperationalHours.text = restaurant.operating_hours?.let {
            "Jam Operasional: ${it.opening_time} - ${it.closing_time}"
        } ?: "Jam Operasional tidak tersedia"

        lifecycleScope.launch(Dispatchers.Main) {
            val isFav = isFavorite(restaurant.id)
            binding.imgFavorite.setImageResource(if (isFav) R.drawable.favorite_bold else R.drawable.favorite_line)
        }
    }

    private fun fetchFeedbackFromGoogleMaps(placeId: String) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.RATING)
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val rating = response.place.rating?.toFloat() ?: 0.0f
                val feedbackList = listOf(
                    Feedback(
                        userName = "Google User",
                        userImage = R.drawable.profile,
                        feedbackTime = "Baru saja",
                        feedbackDescription = "Rating dari Google Maps: ${rating}",
                        rating = rating
                    )
                )
                setupFeedbackRecyclerView(feedbackList)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Gagal mengambil ulasan: ${exception.message}")
                val fallbackFeedback = listOf(
                    Feedback(
                        userName = "Ulasan Tidak Tersedia",
                        userImage = R.drawable.profile,
                        feedbackTime = "N/A",
                        feedbackDescription = "Ulasan tidak ditemukan di Google Maps.",
                        rating = 0.0f
                    )
                )
                setupFeedbackRecyclerView(fallbackFeedback)
            }
    }

    private fun setupFeedbackRecyclerView(feedbackList: List<Feedback>) {
        val feedbackAdapter = FeedbackAdapter(feedbackList)
        binding.itemFeedbackDetail.apply {
            layoutManager = LinearLayoutManager(this@DetailRestoActivity)
            adapter = feedbackAdapter
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
        val db = FavoriteDatabase.getInstance(this)
        val favoriteDao = db.favoriteDao()
        var isFavorite = false

        lifecycleScope.launch(Dispatchers.IO) {
            isFavorite = favoriteDao.getFavoriteById(restaurantId) != null
            launch(Dispatchers.Main) {
                binding.imgFavorite.setImageResource(
                    if (isFavorite) R.drawable.favorite_bold else R.drawable.favorite_line
                )
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
                favoriteDao.addFavorite(
                    FavoriteItem(
                        id = restaurant.id,
                        name = restaurant.name,
                        address = restaurant.address,
                        rating = restaurant.rating.toString(),
                        image = R.drawable.restoran_1
                    )
                )
                runOnUiThread {
                    Toast.makeText(this@DetailRestoActivity, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
                }
            } else {
                favoriteDao.removeFavorite(existingFavorite)
                runOnUiThread {
                    Toast.makeText(this@DetailRestoActivity, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
                }
            }

            runOnUiThread {
                binding.imgFavorite.setImageResource(
                    if (existingFavorite == null) R.drawable.favorite_bold else R.drawable.favorite_line
                )
            }
        }
    }
}
