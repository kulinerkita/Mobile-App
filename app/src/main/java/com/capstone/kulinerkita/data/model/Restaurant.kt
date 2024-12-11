package com.capstone.kulinerkita.data.model

import android.os.Parcel
import android.os.Parcelable

data class Restaurant(
    val id: Int,
    val name: String,
    val address: String,
    val phone_number: String?,
    val min_price: Int,
    val max_price: Int,
    val eco_friendly: Int,
    val categorize_weather: String?,
    val rating: Float?,
    val reviews: Int?,
    val operating_hours: OperatingHours?,
    val placeId: String?,
    val latitude: Double?,
    val longitude: Double?,
    val category_id: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(OperatingHours::class.java.classLoader),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(phone_number)
        parcel.writeInt(min_price)
        parcel.writeInt(max_price)
        parcel.writeInt(eco_friendly)
        parcel.writeString(categorize_weather)
        parcel.writeValue(rating)
        parcel.writeValue(reviews)
        parcel.writeParcelable(operating_hours, flags)
        parcel.writeString(placeId)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeInt(category_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Restaurant> {
        override fun createFromParcel(parcel: Parcel): Restaurant {
            return Restaurant(parcel)
        }

        override fun newArray(size: Int): Array<Restaurant?> {
            return arrayOfNulls(size)
        }
    }
}

data class OperatingHours(
    val opening_time: String,
    val closing_time: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(opening_time)
        parcel.writeString(closing_time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OperatingHours> {
        override fun createFromParcel(parcel: Parcel): OperatingHours {
            return OperatingHours(parcel)
        }

        override fun newArray(size: Int): Array<OperatingHours?> {
            return arrayOfNulls(size)
        }
    }
}
