package com.deniskorotchenko.universityproject.data.network.response.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateProfileErrorResponse(
    @Json(name = "user_name") val userName: List<CodeError>?,
    @Json(name = "first_name") val firstName: List<CodeError>?,
    @Json(name = "last_name") val lastName: List<CodeError>?,
    @Json(name = "email") val email: List<CodeError>?,
    @Json(name = "password") val password: List<CodeError>?,
    @Json(name = "about_me") val aboutMe: List<CodeError>?,
    @Json(name = "picture") val avatarUrl: List<CodeError>?,
    @Json(name = "phone_number") val phoneNumber: List<CodeError>?
)

@JsonClass(generateAdapter = true)
data class CodeError(
    @Json(name = "code") val errorCode: Long,
    @Json(name = "message") val errorMessage: String?
)