package com.example.mealplanb
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
)
