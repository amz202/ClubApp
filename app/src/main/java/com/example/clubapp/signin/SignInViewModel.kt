package com.example.clubapp.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clubapp.ClubApplication
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.network.request.AuthUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface _uiState {
    data class Success(val user: AuthUser) : _uiState
    data object Error : _uiState
    data object Loading : _uiState
}

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    var uiState: _uiState by mutableStateOf(_uiState.Loading)
        private set

    private val _user = MutableStateFlow<AuthUser?>(null)
    val user: StateFlow<AuthUser?> = _user.asStateFlow()

    fun login(token: String) {
        viewModelScope.launch {
            uiState = _uiState.Loading
            try {
                val userResponse = authRepository.login(token)
                println("Server response: $userResponse") // Log the server response

                _user.value = userResponse
                userPreferences.saveUser(userResponse)

                uiState = _uiState.Success(userResponse)
            } catch (e: Exception) {
                uiState = _uiState.Error
                println("AuthViewModel: Login failed - ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClubApplication
                SignInViewModel(app.container.authRepository, app.userPreferences)
            }
        }
    }
}
