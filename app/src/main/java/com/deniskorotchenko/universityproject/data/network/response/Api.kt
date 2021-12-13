package com.deniskorotchenko.universityproject.data.network.response

import com.deniskorotchenko.universityproject.data.network.request.CreateProfileRequest
import com.deniskorotchenko.universityproject.data.network.request.RefreshAuthTokensRequest
import com.deniskorotchenko.universityproject.data.network.request.SignInWithEmailRequest
import com.deniskorotchenko.universityproject.data.network.response.error.*
import com.deniskorotchenko.universityproject.entity.AuthTokens
import com.deniskorotchenko.universityproject.entity.User
import com.haroldadmin.cnradapter.NetworkResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.*

interface Api {
    @GET("users?per_page=10")
    suspend fun getUsers(): GetUsersResponse

    @POST("auth/sign-in-with-email")
    suspend fun signInWithEmail(
        @Body request: SignInWithEmailRequest
    ): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse>

    @POST("auth/refresh-tokens")
    suspend fun refreshAuthTokens(
        @Body request: RefreshAuthTokensRequest
    ): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse>

    @POST("registration/verification-code/send")
    suspend fun sendRegistrationVerificationCode(
        @Query("email") email: String,
    ): NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse>

    @POST("registration/verification-code/verify")
    suspend fun verifyRegistrationCode(
        @Query("code") code: String,
        @Query("email") email: String?
    ): NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse>

    @POST("registration/create-profile")
    suspend fun createProfile(
        @Body request: CreateProfileRequest
    ): NetworkResponse<User, CreateProfileErrorResponse>

    @GET("users/get-profile")
    suspend fun getProfile() :
            NetworkResponse<User, GetUserDataByTokenErrorResponse>
}

@JsonClass(generateAdapter = true)
data class GetUsersResponse(
    @Json(name = "data") val data: List<User>
)