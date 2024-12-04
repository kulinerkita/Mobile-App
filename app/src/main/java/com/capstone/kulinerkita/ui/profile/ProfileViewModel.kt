package com.capstone.kulinerkita.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.capstone.kulinerkita.data.repository.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserByEmail(email: String) = liveData {
        val user = userRepository.getUserByEmail(email)
        emit(user)
    }
}

class ProfileViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
