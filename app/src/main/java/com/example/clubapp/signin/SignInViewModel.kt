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
import com.example.clubapp.ui.viewModels.BaseUiState
import kotlinx.coroutines.launch

typealias SignInUiState = BaseUiState<AuthUser>

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    var uiState: SignInUiState by mutableStateOf(BaseUiState.Loading)
        private set

    fun login(token: String) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val userResponse = authRepository.login(token)
                println("Server response: $userResponse , token: $token") // Log the server response

                userPreferences.saveUser(userResponse, token) //saves in the user prefs

                uiState = BaseUiState.Success(userResponse)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
                println("AuthViewModel: Login failed - ${e.message}")
            }
        }
    }

    companion object {
        val authFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClubApplication
                SignInViewModel(app.container.authRepository, app.userPreferences)
            }
        }
    }
}
