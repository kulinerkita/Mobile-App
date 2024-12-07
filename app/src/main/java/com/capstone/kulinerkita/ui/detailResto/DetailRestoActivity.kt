package com.capstone.kulinerkita.ui.detailResto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.kulinerkita.MapsActivity
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivityDetailRestoBinding
import java.text.DecimalFormat

@Suppress("DEPRECATION")
class DetailRestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRestoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            // Ambil data dari Intent
            val restaurantList = intent.getParcelableArrayListExtra<Restaurant>("RESTAURANT_LIST")
            val selectedRestaurantId = intent.getStringExtra("SELECTED_RESTAURANT_ID")

            Log.d("DetailResto", "Restaurant List Received: $restaurantList")
            Log.d("DetailResto", "Selected Restaurant ID Received: $selectedRestaurantId")

            if (restaurantList.isNullOrEmpty()) {
                Log.e("DetailResto", "Restaurant List is null or empty!")
            }
            if (selectedRestaurantId.isNullOrEmpty()) {
                Log.e("DetailResto", "Selected Restaurant ID is null or empty!")
            }

            // Temukan restoran yang dipilih berdasarkan ID
            Log.d("DetailResto", "Received ID: $selectedRestaurantId")
            val selectedRestaurant = restaurantList!!.find { it.id == selectedRestaurantId!!.toIntOrNull() }
            if (selectedRestaurant == null) {
                Log.e("DetailResto", "Restoran dengan ID $selectedRestaurantId tidak ditemukan")
            }

            if (selectedRestaurant == null) {
                Log.e("DetailResto", "Restoran tidak ditemukan dengan ID: $selectedRestaurantId")
                Toast.makeText(this, "Restoran tidak ditemukan!", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

            // Tampilkan data restoran di UI
            bindRestaurantData(selectedRestaurant)
        } catch (e: Exception) {
            Log.e("DetailResto", "Error pada DetailRestoActivity", e)
            Toast.makeText(this, "Terjadi kesalahan!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun bindRestaurantData(restaurant: Restaurant) {
        try {
            binding.imgResto.setImageResource(restaurant.imageResId)
            binding.tvRestaurantDetail.text = restaurant.name
            binding.tvDetailAddress.text = restaurant.address
            binding.tvPhone.text = restaurant.phoneNumber ?: "Tidak tersedia"

            val format = DecimalFormat("#,###")
            binding.tvPriceRange.text =
                "Rp${format.format(restaurant.minPrice)} - Rp${format.format(restaurant.maxPrice)}"

            binding.tvCategoriEcoDetail.text =
                if (restaurant.ecoFriendly) "Eco-Friendly" else "Non-Eco-Friendly"
            binding.tvCategoriSuhuDetail.text = restaurant.categorizeWeather ?: "Tidak Diketahui"
            binding.tvratingsDetail.text = restaurant.rating?.let { "$it (${restaurant.reviews} reviews)" } ?: "Rating tidak tersedia"
            // Tampilkan jam operasional jika tersedia
            val operationalHours = restaurant.operatingHours
            if (operationalHours != null) {
                val adapter = OperationalHoursAdapter(operationalHours)
                binding.rvOperationalHours.layoutManager = LinearLayoutManager(this)
                binding.rvOperationalHours.adapter = adapter
            }
        } catch (e: Exception) {
            Log.e("DetailResto", "Error binding data restoran", e)
        }

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