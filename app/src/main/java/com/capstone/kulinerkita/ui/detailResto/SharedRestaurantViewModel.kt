package com.capstone.kulinerkita.ui.detailResto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.kulinerkita.data.model.Restaurant

class SharedRestaurantViewModel : ViewModel() {
    private val _restaurantList = MutableLiveData<List<Restaurant>>()
    val restaurantList: LiveData<List<Restaurant>> get() = _restaurantList

    fun setRestaurantList(restaurants: List<Restaurant>) {
        _restaurantList.value = restaurants
    }
}
