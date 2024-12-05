package com.capstone.kulinerkita.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.capstone.kulinerkita.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun registerUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun loginUser(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?
}
