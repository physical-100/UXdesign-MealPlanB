package com.example.mealplanb

import android.os.Parcel
import android.os.Parcelable

data class User_calory(
    val name:String,
    var goal_calory: Int,
    var carb: Int,
    var protein: Int,
    var fat: Int,
    var carb_percent:Int,
    var protein_percent:Int,
    var fat_percent:Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
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
        parcel.writeInt(carb_percent)
        parcel.writeInt(protein_percent)
        parcel.writeInt(fat_percent)

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
        fun clear(): User_calory {
            return User_calory("",0, 0, 0, 0, 0, 0, 0)
        }
    }
}

