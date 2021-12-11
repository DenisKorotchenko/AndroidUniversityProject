package com.deniskorotchenko.universityproject.data.network.response.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.deniskorotchenko.universityproject.entity.Error

@JsonClass(generateAdapter = true)
data class GetUserDataByTokenErrorResponse(
    @Json(name = "non_field_errors") val nonFieldErrors: List<Error>?
)