package com.capstone.kulinerkita.data.repository

import com.capstone.kulinerkita.API.RestaurantApiService
import com.capstone.kulinerkita.data.model.Restaurant
import retrofit2.Response

class RestaurantRepository(private val apiService: RestaurantApiService) {
    suspend fun getRestaurantByCategory(categoryId: Int): Response<List<Restaurant>> {
        return apiService.getRestaurantsByCategory(categoryId)
    }
}
