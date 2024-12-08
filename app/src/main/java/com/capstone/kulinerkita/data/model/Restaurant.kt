package com.capstone.kulinerkita.data.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "restaurant")
data class Restaurant(
    val id: Int,
    val imageResId: Int,
    val name: String,
    val address: String,
    val phone_number: String?,
    val latitude: Double,
    val longitude: Double,
    val category_id: Int,
    val categorize_weather: String?,
    val maps_url: String?,
    val min_price: Int,
    val max_price: Int,
    val kecamatan_id: Int?,
    val eco_friendly: Int,
    val rating: String?,
    val reviews: Int?,
    val operating_hours: OperatingHours?,
    val placeId: String?
) : Parcelable

@Parcelize
data class OperatingHours(
    val opening_time: String,
    val closing_time: String
) : Parcelable