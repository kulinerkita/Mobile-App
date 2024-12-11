package com.capstone.kulinerkita.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.kulinerkita.API.MLApiClient
import com.capstone.kulinerkita.data.model.Restaurant
import com.capstone.kulinerkita.weather.ApiConfig
import com.capstone.kulinerkita.weather.WeatherResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    // Lokasi tetap
    val latitude = -7.56217192454353
    val longitude = 110.83948552592317

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> get() = _weatherData

    private val _restaurantRecommendations = MutableLiveData<List<Restaurant>>()
    val restaurantRecommendations: LiveData<List<Restaurant>> get() = _restaurantRecommendations

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.weatherService.getWeather(city, apiKey)
                _weatherData.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk mengambil rekomendasi restoran
    fun fetchRestaurantRecommendations(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                // Membuat map lokasi untuk dikirim ke API
                val location = mapOf("latitude" to latitude, "longitude" to longitude)
                val response = MLApiClient.retrofitService.getRecommendedRestaurants(location)
                println("ErrorResponse: $response")
                if (response.isSuccessful) {
                    _restaurantRecommendations.value = response.body() ?: emptyList()
                } else {
                    // Menangani error jika response tidak berhasil
                    println("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}