package com.capstone.kulinerkita.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteItem(
    @PrimaryKey val id: Int,
    val name: String,
    val address: String,
    val rating: String?,
    val image: Int
)
