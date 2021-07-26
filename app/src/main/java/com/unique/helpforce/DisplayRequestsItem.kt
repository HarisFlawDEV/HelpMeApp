package com.unique.helpforce

data class DisplayRequestsItem(
    val fname : String,
    val lname : String,
    val status : String,
    val uid : String,
    val requestid : String,
    val Requesttype : Boolean, // true for service , false for help
    val requestDescription : String?,// Both Services & Help
    val coordinates : List<Double>,
    val image : String,
    val phone : String
) {
}