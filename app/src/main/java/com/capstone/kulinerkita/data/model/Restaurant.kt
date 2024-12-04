package com.capstone.kulinerkita.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    val name: String,
    val address: String,
    val categorySuhu: String,
    val categoryEco: String,
    val ratings: String,
    val imageResId: Int,
    val operationalHours: Map<String, String>
) : Parcelable
