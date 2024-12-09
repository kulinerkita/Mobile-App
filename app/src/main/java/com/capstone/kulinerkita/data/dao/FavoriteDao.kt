package com.capstone.kulinerkita.data.dao

import androidx.room.*
import com.capstone.kulinerkita.data.model.FavoriteItem

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): List<FavoriteItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favorite: FavoriteItem)

    @Delete
    fun removeFavorite(favorite: FavoriteItem)

    @Query("SELECT * FROM favorites WHERE id = :id LIMIT 1")
    fun getFavoriteById(id: Int): FavoriteItem?
}
