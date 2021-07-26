package com.unique.helpforce.retrofit

data class ImageUploadResponse(
    val fieldname : String,
    val originalname : String ,
    val encoding : String,
    val mimetype : String,
    val destination: String,
    val filename: String,
    val path : String,
    val size : Int
) {}
