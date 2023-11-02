package com.example.mealplanb

import android.os.Parcel
import android.os.Parcelable

data class Userdata(
    val username: String,
    val gender: String,
    val age: Int,
    val height: Int,
    val start_weight: Double,
    val goal_weight: Double,
    val type: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(gender)
        parcel.writeInt(age)
        parcel.writeInt(height)
        parcel.writeDouble(start_weight)
        parcel.writeDouble(goal_weight)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Userdata> {
        override fun createFromParcel(parcel: Parcel): Userdata {
            return Userdata(parcel)
        }

        override fun newArray(size: Int): Array<Userdata?> {
            return arrayOfNulls(size)
        }
        fun clear(): Userdata {
            return Userdata("", "", 0, 0, 0.0, 0.0, "")
        }
    }
}

