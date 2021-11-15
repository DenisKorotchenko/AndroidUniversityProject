package com.deniskorotchenko.universityproject.ui

import com.deniskorotchenko.universityproject.repository.AuthRepository
import com.deniskorotchenko.universityproject.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class MainViewModel : BaseViewModel() {

    val isAuthorized: Flow<Boolean> = AuthRepository.isAuthorizedFlow

}