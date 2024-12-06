package com.capstone.kulinerkita.API

import com.capstone.kulinerkita.data.model.Restaurant
import retrofit2.Response
import retrofit2.http.GET

interface RestaurantApiService {
    @GET("restaurants/search")
    suspend fun getRestaurants(): Response<List<Restaurant>>
}