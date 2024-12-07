package com.capstone.kulinerkita.ui.detailResto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.kulinerkita.MapsActivity
import com.capstone.kulinerkita.data.model.OperatingHours
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

        // Ambil data dari Intent
        val restaurantList = intent.getParcelableArrayListExtra<Restaurant>("RESTAURANT_LIST")
        val selectedRestaurantId = intent.getStringExtra("SELECTED_RESTAURANT_ID")

        Log.d("DetailResto", "Restaurant List: $restaurantList")
        Log.d("DetailResto", "Selected Restaurant ID: $selectedRestaurantId")

        val selectedRestaurant = restaurantList?.find { it.id == selectedRestaurantId?.toIntOrNull() }
        if (selectedRestaurant == null) {
            Log.e("DetailResto", "Restoran tidak ditemukan!")
            finish() // Menutup halaman jika data tidak ditemukan
            return
        }

        // Tampilkan data di UI
        selectedRestaurant.let { restaurant ->
            binding.imgResto.setImageResource(restaurant.imageResId)
            binding.tvRestaurantDetail.text = restaurant.name
            binding.tvDetailAddress.text = restaurant.address
            binding.tvPhone.text = restaurant.phoneNumber ?: "Tidak tersedia"

            val format = DecimalFormat("#,###")
            binding.tvPriceRange.text = "Rp${format.format(restaurant.minPrice)} - Rp${format.format(restaurant.maxPrice)}"

            binding.tvCategoriEcoDetail.text = if (restaurant.ecoFriendly) "Eco-Friendly" else "Non-Eco-Friendly"
            binding.tvCategoriSuhuDetail.text = restaurant.categorizeWeather ?: "Tidak Diketahui"
            binding.tvratingsDetail.text = "${restaurant.rating} (${restaurant.reviews} reviews)"

            // Bind jam operasional
            val operationalHours = restaurant.operatingHours
            if (operationalHours != null) {
                val adapter = OperationalHoursAdapter(operationalHours)
                binding.rvOperationalHours.layoutManager = LinearLayoutManager(this)
                binding.rvOperationalHours.adapter = adapter
            }
        }

        // Tombol kembali
        binding.backButtonDetail.setOnClickListener {
            finish()
        }

        // Tombol cek maps
        binding.ButtonCekLokasi.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}