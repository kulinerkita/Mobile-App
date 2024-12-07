package com.capstone.kulinerkita.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.databinding.ActivitySearchBinding
import com.capstone.kulinerkita.ui.detailResto.DetailRestoActivity
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
        restaurantList = intent.getParcelableArrayListExtra("RESTAURANT_LIST") ?: listOf()

        // Setup RecyclerView
        restaurantAdapter = HomeAdapter(restaurantList.toMutableList()) { selectedRestaurant ->
            // Handle item klik, misalnya navigasi ke DetailRestoActivity
            startActivity(Intent(this, DetailRestoActivity::class.java).apply {
                 putExtra("SELECTED_RESTAURANT_ID", selectedRestaurant.id)
             })
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
            it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
        }
        restaurantAdapter.updateData(filteredList)

        // Tampilkan pesan jika hasil pencarian kosong
        if (filteredList.isEmpty()) {
            binding.tvNoResults.visibility = View.VISIBLE
        } else {
            binding.tvNoResults.visibility = View.GONE
        }
    }
}
