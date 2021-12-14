package com.deniskorotchenko.universityproject.data.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateProfileRequest(
    //@Json(name = "verification_token") val verificationToken: String,
    @Json(name = "user_name") val userName: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,

    @Json(name = "picture") val avatarUrl: String?,
    @Json(name = "about_me") val aboutMe: String?,
    @Json(name = "phone_number") val phoneNumber: String?
)