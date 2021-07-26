package com.unique.helpforce.retrofit

class GetProfileResponse(
    val image : String ,
    val friends : Array<Friends>,
    val _id : String ,
    val phone : String ,
    val email : String,
    val firstName : String ,
    val lastName : String ,
    val gender : String ,
    val  dob : String ,
    val reviews : Array<Reviews>
    ) {}