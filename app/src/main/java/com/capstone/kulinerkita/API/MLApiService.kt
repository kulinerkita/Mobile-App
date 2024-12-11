package com.capstone.kulinerkita.API

import com.capstone.kulinerkita.data.model.RestaurantResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MLApiService {
    @POST("predict")
    suspend fun getRecommendedRestaurants(@Body location: Map<String, Double>): Response<RestaurantResponse>
}
