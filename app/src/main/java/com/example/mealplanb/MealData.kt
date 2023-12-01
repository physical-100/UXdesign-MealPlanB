package com.example.mealplanb

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MealData (
    val date:String,
    val mealname:String,
    val foodname: String,
    val foodbrand: String?,
    var foodcal:Double,
    var foodamount:Double,
    var foodcarbo:Double,
    var foodprotein:Double,
    var foodfat:Double
): Parcelable
