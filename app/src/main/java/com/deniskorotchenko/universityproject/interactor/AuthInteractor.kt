package com.deniskorotchenko.universityproject.interactor

import com.deniskorotchenko.universityproject.data.network.response.error.CreateProfileErrorResponse
import com.deniskorotchenko.universityproject.data.network.response.error.SignInWithEmailErrorResponse
import com.deniskorotchenko.universityproject.entity.AuthTokens
import com.deniskorotchenko.universityproject.entity.User
import com.deniskorotchenko.universityproject.repository.AuthRepository
import com.deniskorotchenko.universityproject.repository.AuthRepositoryOld
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun isAuthorized(): Flow<Boolean> =
        authRepository.isAuthorizedFlow()

    suspend fun signInWithEmail(email: String, password: String): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse> {
        val response = authRepository.generateAuthTokensByEmail(email, password)
        when (response) {
            is NetworkResponse.Success -> {
                authRepository.saveAuthTokens(response.body)
            }
            is NetworkResponse.Error -> {
                Timber.e(response.error)
            }
        }
        return response
    }

    suspend fun logout() {
        authRepository.saveAuthTokens(null)
    }

    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        userName: String,
        aboutMe: String,
        avatarUrl: String,
    ): NetworkResponse<User, CreateProfileErrorResponse> {
        val response = authRepository.signUp(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName,
            userName = userName,
            aboutMe = aboutMe,
            avatarUrl = avatarUrl
        )
        if (response is NetworkResponse.Error) {
            Timber.e(response.error)
        }
        return response
    }
}