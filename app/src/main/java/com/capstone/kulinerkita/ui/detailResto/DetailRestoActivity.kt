package com.capstone.kulinerkita.ui.detailResto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.MapsActivity
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivityDetailRestoBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.DecimalFormat

class DetailRestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRestoBinding
    private var selectedRestaurant: Restaurant? = null // Tambahkan properti untuk restoran yang dipilih
    private val TAG = "DetailRestoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedRestaurantId = intent.getIntExtra("SELECTED_RESTAURANT_ID", -1)
        Log.d(TAG, "Selected Restaurant ID: $selectedRestaurantId")

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
        } else {
            Log.e(TAG, "Invalid Restaurant ID: $selectedRestaurantId")
            Toast.makeText(this, "ID restoran tidak valid!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Tombol kembali
        binding.backButtonDetail.setOnClickListener {
            finish()
        }

        // Tombol cek maps
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
            val gson = Gson()
            val type = object : TypeToken<List<Restaurant>>() {}.type
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

        val format = DecimalFormat("#,###")
        binding.tvPriceRange.text =
            "Rp${format.format(restaurant.min_price)} - Rp${format.format(restaurant.max_price)}"

        binding.tvCategoriEcoDetail.text =
            if (restaurant.eco_friendly == 1) "Eco-Friendly" else "Non-Eco-Friendly"

        binding.tvCategoriSuhuDetail.text = restaurant.categorize_weather ?: "Tidak Diketahui"
        binding.tvratingsDetail.text =
            restaurant.rating?.let { "$it (${restaurant.reviews} reviews)" } ?: "Rating tidak tersedia"

        val operationalHours = restaurant.operating_hours
        if (operationalHours != null) {
            Log.d(TAG, "Operating Hours: ${operationalHours.opening_time} - ${operationalHours.closing_time}")
            val hoursText = "Buka: ${operationalHours.opening_time} - Tutup: ${operationalHours.closing_time}"
            binding.tvOperationalHours.text = hoursText
        } else {
            Log.e(TAG, "Operating Hours are null")
            binding.tvOperationalHours.text = "Waktu operasional tidak tersedia"
        }
    }
}
