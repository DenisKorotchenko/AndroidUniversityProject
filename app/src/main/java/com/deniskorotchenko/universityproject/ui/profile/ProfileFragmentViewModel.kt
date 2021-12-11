package com.deniskorotchenko.universityproject.ui.profile

import androidx.lifecycle.viewModelScope
import com.deniskorotchenko.universityproject.data.network.response.Api
import com.deniskorotchenko.universityproject.data.network.response.error.GetUserDataByTokenErrorResponse
import com.deniskorotchenko.universityproject.entity.User
import com.deniskorotchenko.universityproject.interactor.AuthInteractor
import com.deniskorotchenko.universityproject.ui.base.BaseViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val providerApi: Api
): BaseViewModel() {

    sealed class ViewState {
        object Loading : ViewState()
        data class Data(val userData: User) : ViewState()
        object Error: ViewState()
    }

    sealed class Event {
        data class LogoutError(val error: Throwable): Event()
    }

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState: Flow<ViewState> get() = _viewState.asStateFlow()
    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    init {
        viewModelScope.launch {
            _viewState.emit(ViewState.Loading)
            var found = false
            for (attempt in 0..3) {
                when (val userDataResponse = loadUserData()) {
                    is NetworkResponse.Success -> {
                        _viewState.emit(ViewState.Data(userDataResponse.body))
                        found = true
                        break
                    }
                    is NetworkResponse.Error -> {
                        Timber.e(userDataResponse.error)
                    }
                }
            }
            if (!found) {
                _viewState.emit(ViewState.Error)
            }
        }
    }

    private suspend fun loadUserData(): NetworkResponse<User, GetUserDataByTokenErrorResponse> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(3000) // моделирование долгой загрузки
            providerApi.getProfile()
        }
    }

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authInteractor.logout()
            } catch (error: Throwable) {
                Timber.e(error)
                _eventChannel.send(Event.LogoutError(error))
            }
        }
    }

}