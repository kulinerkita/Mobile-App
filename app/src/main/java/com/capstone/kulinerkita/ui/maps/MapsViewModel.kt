package com.capstone.kulinerkita.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.data.repository.RestaurantRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: RestaurantRepository) : ViewModel() {
    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    fun fetchRestaurants(categoryId: Int) {
        viewModelScope.launch {
            val response = repository.getRestaurantByCategory(categoryId)
            if (response.isSuccessful) {
                _restaurants.value = response.body() ?: emptyList()
            }
        }
    }
}
