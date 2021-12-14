package com.deniskorotchenko.universityproject.ui.signup

import androidx.lifecycle.viewModelScope
import com.deniskorotchenko.universityproject.data.network.response.error.CreateProfileErrorResponse
import com.deniskorotchenko.universityproject.interactor.AuthInteractor
import com.deniskorotchenko.universityproject.ui.base.BaseViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {
    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow() : Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    sealed class Event {
        object SignUpSuccess : Event()
        object SignUpEmailConfirmationRequired : Event()
        object NetworkError : Event()
        object UnknownError: Event()
        data class SingUpServerError(val error: CreateProfileErrorResponse): Event()
    }

    fun signUp(
        firstName: String,
        lastName: String,
        userName: String,
        email: String,
        password: String,
        aboutMe: String,
        avatarUrl: String
    ) {
        viewModelScope.launch {
            try {
                val response = authInteractor.signUp(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    userName = userName,
                    aboutMe = aboutMe,
                    avatarUrl = avatarUrl
                )
                when (response) {
                    is NetworkResponse.Success -> {
                        _eventChannel.send(Event.SignUpSuccess)
                    }
                    is NetworkResponse.ServerError -> {
                        val error = response.body
                        if (error != null) {
                            _eventChannel.send(Event.SingUpServerError(error))
                        } else {
                            _eventChannel.send(Event.UnknownError)
                        }
                    }
                    is NetworkResponse.NetworkError -> {
                        _eventChannel.send(Event.NetworkError)
                    }
                    is NetworkResponse.UnknownError -> {
                        _eventChannel.send(Event.UnknownError)
                    }
                }
                // сообщать про необходимость подтвержения email
                //_eventChannel.send(Event.SignUpEmailConfirmationRequired)
            } catch (e: Exception) {
                _eventChannel.send(Event.UnknownError)
            }
        }
    }
}