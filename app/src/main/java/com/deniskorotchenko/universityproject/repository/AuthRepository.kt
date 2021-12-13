package com.deniskorotchenko.universityproject.repository

import com.deniskorotchenko.universityproject.data.network.request.CreateProfileRequest
import com.deniskorotchenko.universityproject.data.network.request.RefreshAuthTokensRequest
import com.deniskorotchenko.universityproject.data.network.request.SignInWithEmailRequest
import com.deniskorotchenko.universityproject.data.network.response.Api
import com.deniskorotchenko.universityproject.data.network.response.error.CreateProfileErrorResponse
import com.deniskorotchenko.universityproject.data.network.response.error.RefreshAuthTokensErrorResponse
import com.deniskorotchenko.universityproject.data.network.response.error.SignInWithEmailErrorResponse
import com.deniskorotchenko.universityproject.data.persistent.LocalKeyValueStorage
import com.deniskorotchenko.universityproject.di.AppCoroutineScope
import com.deniskorotchenko.universityproject.di.IoCoroutineDispatcher
import com.deniskorotchenko.universityproject.entity.AuthTokens
import com.deniskorotchenko.universityproject.entity.User
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiLazy: Lazy<Api>,
    private val localKeyValueStorage: LocalKeyValueStorage,
    @AppCoroutineScope externalCoroutineScope: CoroutineScope,
    @IoCoroutineDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val api by lazy { apiLazy.get() }
    private val authTokensFlow: Deferred<MutableStateFlow<AuthTokens?>> =
        externalCoroutineScope.async(context = ioDispatcher, start = CoroutineStart.LAZY) {
            Timber.d("Initializing auth tokens flow.")
            MutableStateFlow(
                localKeyValueStorage.authTokens
            )
        }

    suspend fun getAuthTokensFlow(): StateFlow<AuthTokens?> {
        return authTokensFlow.await().asStateFlow()
    }

    /**
     * @param authTokens active auth tokens which must be used for signing all requests
     */
    suspend fun saveAuthTokens(authTokens: AuthTokens?) {
        withContext(ioDispatcher) {
            Timber.d("Persist auth tokens $authTokens.")
            localKeyValueStorage.authTokens = authTokens
        }
        Timber.d("Emit auth tokens $authTokens.")
        authTokensFlow.await().emit(authTokens)
    }

    /**
     * @return whether active access tokens are authorized or not
     */
    suspend fun isAuthorizedFlow(): Flow<Boolean> {
        return authTokensFlow
            .await()
            .asStateFlow()
            .map { it != null }
    }

    suspend fun generateAuthTokensByEmail(
        email: String,
        password: String
    ): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse> {
        return api.signInWithEmail(SignInWithEmailRequest(email, password))
    }

    /**
     * Creates a user account in the system as a side effect.
     * @return access tokens with higher permissions for the new registered user
     */
    suspend fun generateAuthTokensByEmailAndPersonalInfo(
        email: String,
        verificationToken: String,
        firstName: String,
        lastName: String,
        password: String
    ): NetworkResponse<AuthTokens, CreateProfileErrorResponse> {
        TODO("Later")
//        return api.createProfile(
//            CreateProfileRequest(
//                verificationToken,
//                firstName,
//                lastName,
//                email,
//                password
//            )
//        )
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
        return api.createProfile(
            CreateProfileRequest(
                userName = userName,
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                aboutMe = if (aboutMe != "") aboutMe else null,
                avatarUrl = if (avatarUrl != "") avatarUrl else null,
                phoneNumber = null
            )
        )
    }

    suspend fun generateRefreshedAuthTokens(refreshToken: String): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse> {
        return api.refreshAuthTokens(RefreshAuthTokensRequest(refreshToken))
    }
}