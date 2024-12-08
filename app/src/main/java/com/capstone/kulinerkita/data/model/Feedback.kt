package com.capstone.kulinerkita.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feedback(
    val userName: String,
    val userImage: Int,
    val feedbackTime: String,
    val feedbackDescription: String,
    val rating: Float
) : Parcelable
