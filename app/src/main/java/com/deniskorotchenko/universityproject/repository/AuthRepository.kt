package com.deniskorotchenko.universityproject.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthRepository {
    private val _isAuthorizedFlow = MutableStateFlow(false)
    val isAuthorizedFlow = _isAuthorizedFlow.asStateFlow()

    suspend fun signIn() {
        _isAuthorizedFlow.emit(true)
    }

    suspend fun logOut() {
        _isAuthorizedFlow.emit(false)
    }
}