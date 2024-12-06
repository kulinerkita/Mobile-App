package com.capstone.kulinerkita.ui.detailResto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivityDetailRestoBinding

@Suppress("DEPRECATION")
class DetailRestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRestoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val restaurant = intent.getParcelableExtra<Restaurant>("EXTRA_RESTAURANT")
        // Tampilkan data
        restaurant?.let {
            binding.tvRestaurantDetail.text = it.name
            binding.tvDetailAddress.text = it.address
            binding.tvCategoriSuhuDetail.text = it.categorizeWeather
            binding.tvCategoriEcoDetail.text = it.ecoFriendly.toString()
            binding.tvratingsDetail.text = it.rating
            binding.imgResto.setImageResource(it.imageResId)

            // Setup RecyclerView untuk jam operasional
            val operationalHoursAdapter = OperationalHoursAdapter(it.operatingHours)
            binding.rvOperationalHours.adapter = operationalHoursAdapter
        }

        // Tombol kembali
        binding.backButtonDetail.setOnClickListener {
            finish()
        }
    }
}