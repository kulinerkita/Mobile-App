package com.capstone.kulinerkita.ui.detailResto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.MapsActivity
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivityDetailRestoBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailRestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRestoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRestoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedRestaurantId = intent.getIntExtra("SELECTED_RESTAURANT_ID", -1)
        if (selectedRestaurantId != -1) {
            val selectedRestaurant = getRestaurantFromCache(selectedRestaurantId)
            if (selectedRestaurant != null) {
                bindRestaurantData(selectedRestaurant)
            } else {
                Toast.makeText(this, "Restoran tidak ditemukan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "ID restoran tidak valid!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getRestaurantFromCache(restaurantId: Int): Restaurant? {
        val sharedPreferences = getSharedPreferences("AppCache", Context.MODE_PRIVATE)
        val restaurantListJson = sharedPreferences.getString("RESTAURANT_LIST", null)

        return if (restaurantListJson != null) {
            // Konversi JSON kembali ke List<Restaurant>
            val gson = Gson()
            val type = object : TypeToken<List<Restaurant>>() {}.type
            val restaurantList: List<Restaurant> = gson.fromJson(restaurantListJson, type)

            // Cari restoran berdasarkan ID
            restaurantList.find { it.id == restaurantId }
        } else {
            null
        }
    }

    private fun bindRestaurantData(restaurant: Restaurant) {
        binding.tvRestaurantDetail.text = restaurant.name
        binding.tvDetailAddress.text = restaurant.address
        binding.tvPhone.text = restaurant.phone_number ?: "Tidak tersedia"
        binding.tvPriceRange.text = "Rp${restaurant.minPrice} - Rp${restaurant.maxPrice}"


        // Tombol kembali
        binding.backButtonDetail.setOnClickListener {
            finish()
        }

        // Tombol cek maps
        binding.ButtonCekLokasi.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java).apply {
                putExtra("LATITUDE", restaurant.latitude)
                putExtra("LONGITUDE", restaurant.longitude)
            }
            startActivity(intent)
        }
    }
}