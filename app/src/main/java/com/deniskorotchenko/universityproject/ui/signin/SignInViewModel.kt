package com.deniskorotchenko.universityproject.ui.signin

import androidx.lifecycle.viewModelScope
import com.deniskorotchenko.universityproject.repository.AuthRepository
import com.deniskorotchenko.universityproject.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel : BaseViewModel() {
    fun signIn() {
        viewModelScope.launch {
            AuthRepository.signIn()
        }
    }

}