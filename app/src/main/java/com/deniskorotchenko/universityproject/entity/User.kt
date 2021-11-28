package com.deniskorotchenko.universityproject.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "avatar") val avatarUrl: String, // URL to image in format like this: "https//test.com/test.jpg"
    @Json(name = "first_name") val userName: String,
    @Json(name = "email") val groupName: String
)
