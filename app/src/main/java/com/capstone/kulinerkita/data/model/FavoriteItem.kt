package com.capstone.kulinerkita.data.model

data class FavoriteItem(
    val image: Int,
    val name: String,
    val address: String,
    val rating: Double,
    val categories: List<String>
)
