package com.capstone.kulinerkita.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantDescription(
    val idDesc: Int,
    val description: String
) : Parcelable
