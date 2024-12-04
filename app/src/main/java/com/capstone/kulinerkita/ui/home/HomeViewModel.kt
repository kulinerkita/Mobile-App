package com.capstone.kulinerkita.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.kulinerkita.weather.ApiConfig
import com.capstone.kulinerkita.weather.WeatherResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> get() = _weatherData

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
}