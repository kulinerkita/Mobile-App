package com.capstone.kulinerkita.API

import com.capstone.kulinerkita.data.model.Restaurant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantApiService {
    @GET("restaurants/search")
    suspend fun getRestaurants(): Response<List<Restaurant>>

    @GET("restaurants/search")
    suspend fun getRestaurantsByCategoryAndWeather(
        @Query("categoryId") categoryId: Int,
        @Query("weather") categorizeWeather: String? = null // Parameter weather bisa null
    ): Response<List<Restaurant>>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: Int): Response<Restaurant>
}