package com.capstone.kulinerkita.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categorise(
    val idCategorise: Int,
    val namaCategorise: String,
    val typeCategorise: String,
    val gamblerCategorise: Int
) : Parcelable
