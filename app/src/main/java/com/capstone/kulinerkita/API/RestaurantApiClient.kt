package com.capstone.kulinerkita.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestaurantApiClient {
    private const val BASE_URL = "https://kulinerkita.et.r.appspot.com/"

    val RestaurantApi: RestaurantApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestaurantApiService::class.java)
    }
}
