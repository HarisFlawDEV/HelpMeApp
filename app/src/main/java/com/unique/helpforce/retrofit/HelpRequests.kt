package com.unique.helpforce.retrofit

data class HelpRequests(
    val status : String,
    val urgencyLevel : String,
    val nearbyUsersAllowed : Boolean,
    val _id : String,
    val creatorId : CreatorId,
    val description : String,
    val createdAt : String,
    val updatedAt : String,
    val __v : Int
) {}
