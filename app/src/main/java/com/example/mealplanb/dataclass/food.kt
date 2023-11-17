package com.example.mealplanb.dataclass

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class food(
    var foodname: String?,
    var foodbrand: String?,
    var foodcal:Double,
    var foodamount:Double,
    var foodcarbo:Double,
    var foodprotein:Double,
    var foodfat:Double
):
    Parcelable