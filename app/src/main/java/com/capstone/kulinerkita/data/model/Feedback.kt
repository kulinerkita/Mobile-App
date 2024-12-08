package com.capstone.kulinerkita.data.model

data class Feedback(
    val userName: String,
    val userImage: Int, // Resource ID untuk gambar
    val feedbackTime: String,
    val feedbackDescription: String,
    val rating: Float
)