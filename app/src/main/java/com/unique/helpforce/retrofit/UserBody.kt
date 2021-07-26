package com.unique.helpforce.retrofit

data class UserBody(val phone: String,
                    val password: String,
                    val confirmPassword: String,
                    val firstName: String,
                    val lastName: String,
                    val email : String,
                    val dob : String,
                    val gender : String)