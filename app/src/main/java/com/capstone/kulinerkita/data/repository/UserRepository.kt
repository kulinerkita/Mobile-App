package com.capstone.kulinerkita.data.repository

import com.capstone.kulinerkita.data.dao.UserDao
import com.capstone.kulinerkita.data.model.User

class UserRepository(private val userDao: UserDao) {
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}