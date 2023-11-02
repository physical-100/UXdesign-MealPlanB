package com.example.mealplanb

import android.os.Parcel
import android.os.Parcelable

data class User_calory(val goal_calory: Int, val carb: Int, val protein: Int, val fat: Int) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(goal_calory)
        parcel.writeInt(carb)
        parcel.writeInt(protein)
        parcel.writeInt(fat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User_calory> {
        override fun createFromParcel(parcel: Parcel): User_calory {
            return User_calory(parcel)
        }

        override fun newArray(size: Int): Array<User_calory?> {
            return arrayOfNulls(size)
        }
    }
}

