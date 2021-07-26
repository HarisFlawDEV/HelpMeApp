package com.unique.helpforce.retrofit

data class HelpRequestResponse(val status: String,val urgencyLevel :String , val nearbyUsersAllowed : Boolean,val _id : String,val creatorId: String,val description : String,val requestedTo : Array<RequestTo> ,val createdAt :String, val updatedAt : String,val __v: Int ) {}
