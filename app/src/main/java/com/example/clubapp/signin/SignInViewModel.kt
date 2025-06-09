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

/*
 * Copyright 2025 Abdul Majid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

typealias SignInUiState = BaseUiState<AuthUser?>

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    var uiState: SignInUiState by mutableStateOf(BaseUiState.Success(null))
        private set

    fun login(token: String) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val userResponse = authRepository.login(token)
                println("Server response: $userResponse , token: $token")

                userPreferences.saveUser(userResponse, token) //saves in the user prefs

                uiState = BaseUiState.Success(userResponse)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
                println("AuthViewModel: Login failed - ${e.message}")
            }
        }
    }

    fun resetSignInState(){
        uiState = BaseUiState.Success(null)
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
