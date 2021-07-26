package com.unique.helpforce.retrofit

data class ServiceRequests(
    val status : String,
    val _id : String,
    val finderId: FinderId,
    val serviceProviderId : String,
    val createdAt : String,
    val updatedAt : String,
    val __v :  Int) {}