package com.capstone.kulinerkita.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MLApiClient{
// Base URL untuk API ML
private const val BASE_URL_ML = "https://ml-model-predict-636814706436.asia-southeast2.run.app/"
val retrofitService: MLApiService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL_ML)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MLApiService::class.java)
}
}