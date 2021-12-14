package com.deniskorotchenko.universityproject.data.network.response

import com.deniskorotchenko.universityproject.data.network.request.CreateProfileRequest
import com.deniskorotchenko.universityproject.data.network.request.RefreshAuthTokensRequest
import com.deniskorotchenko.universityproject.data.network.request.SignInWithEmailRequest
import com.deniskorotchenko.universityproject.data.network.response.error.*
import com.deniskorotchenko.universityproject.entity.AuthTokens
import com.deniskorotchenko.universityproject.entity.Post
import com.deniskorotchenko.universityproject.entity.User
import com.haroldadmin.cnradapter.NetworkResponse

class MockApi: Api {
    override suspend fun getUsers(): GetUsersResponse {
        return GetUsersResponse(listOf())
    }

    override suspend fun signInWithEmail(request: SignInWithEmailRequest): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse> {
        return NetworkResponse.Success(
            AuthTokens(
                accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2MzgsImV4cCI6MTY0MDg3MTc3MX0.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyHI",
                refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2MzgsImV4cCI6MTY0MDg3MTc3MX0.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyHI",
                accessTokenExpiration = 1640871771000,
                refreshTokenExpiration = 1640871771000,
            ),
            code = 200
        )
    }
    override suspend fun refreshAuthTokens(request: RefreshAuthTokensRequest): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun sendRegistrationVerificationCode(email: String): NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyRegistrationCode(
        code: String,
        email: String?
    ): NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createProfile(request: CreateProfileRequest): NetworkResponse<User, CreateProfileErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPosts(): NetworkResponse<List<Post>, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(): NetworkResponse<User, GetUserDataByTokenErrorResponse> {
        return NetworkResponse.Success(
            User(
                id = 0,
                avatarUrl = "https://reqres.in/img/faces/2-image.jpg",
                userName = "JanetWeaver20",
                firstName = "Janet",
                lastName = "Weaver"
            ),
            code = 200
        )
    }
}