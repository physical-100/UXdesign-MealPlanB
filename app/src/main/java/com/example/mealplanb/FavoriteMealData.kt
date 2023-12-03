package com.example.mealplanb

data class FavoriteMealData (
    val foodkey:String,
    val foodname: String,
    val foodbrand: String?,
    var foodcal:Double,
    var foodamount:Double,
    var foodcarbo:Double,
    var foodprotein:Double,
    var foodfat:Double
)
