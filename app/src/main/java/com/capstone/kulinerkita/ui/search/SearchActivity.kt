package com.capstone.kulinerkita.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivitySearchBinding
import com.capstone.kulinerkita.ui.home.HomeAdapter

@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var restaurantAdapter: HomeAdapter
    private var restaurantList: List<Restaurant> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dapatkan data restoran dari Intent
        restaurantList = intent.getParcelableArrayListExtra("restaurantList") ?: listOf()

        // Setup RecyclerView
        restaurantAdapter = HomeAdapter(restaurantList.toMutableList()) {
            // Handle item click jika diperlukan
            // Misalnya: Buka DetailActivity
            // startActivity(Intent(this, DetailActivity::class.java).apply {
            //     putExtra("restaurant", restaurant)
            // })
        }

        binding.rvSearchResults.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            adapter = restaurantAdapter
        }

        // Tambahkan event listener untuk input pencarian
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRestaurant(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterRestaurant(query: String) {
        val filteredList = restaurantList.filter {
            it.name.contains(query, ignoreCase = true)
        }
        restaurantAdapter.updateData(filteredList)
    }
}
