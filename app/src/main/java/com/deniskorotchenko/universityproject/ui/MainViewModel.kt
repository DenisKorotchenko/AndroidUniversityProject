package com.deniskorotchenko.universityproject.ui

import com.deniskorotchenko.universityproject.interactor.AuthInteractor
import com.deniskorotchenko.universityproject.repository.AuthRepositoryOld
import com.deniskorotchenko.universityproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
): BaseViewModel() {

    suspend fun isAuthorizedFlow(): Flow<Boolean> =
        authInteractor.isAuthorized()

}