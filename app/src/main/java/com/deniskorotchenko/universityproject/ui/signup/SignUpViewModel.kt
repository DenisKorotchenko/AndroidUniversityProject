package com.deniskorotchenko.universityproject.ui.signup

import androidx.lifecycle.viewModelScope
import com.deniskorotchenko.universityproject.repository.AuthRepository
import com.deniskorotchenko.universityproject.ui.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel : BaseViewModel() {
    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow() : Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    sealed class Event {
        object SignUpSuccess : Event()
        object SignUpEmailConfirmationRequired : Event()
    }

    fun signUp(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                AuthRepository.signUp(
                    firstname,
                    lastname,
                    nickname,
                    email,
                    password
                )
                // сообщить, что операция регистрации успешна
                //_eventChannel.send(Event.SignUpSuccess)
                //debug!
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            } catch (e: Exception) {
                // сообщить пользователю, что емэил не был подтверждён
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            }
        }
    }
}