package com.capstone.kulinerkita.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    val id: Int,
    val imageResId: Int,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val latitude: Double,
    val longitude: Double,
    val categoryId: Int,
    val categorizeWeather: String,
    val mapsUrl: String,
    val minPrice: Int,
    val maxPrice: Int,
    val kecamatanId: Int,
    val ecoFriendly: Boolean,
    val rating: String,
    val reviews: Int,
    val operatingHours: OperatingHours
) : Parcelable

@Parcelize
data class OperatingHours(
    val hours: Map<String, String> // Map dengan key hari dan value jam operasional
) : Parcelable

