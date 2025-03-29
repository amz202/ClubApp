package com.example.clubapp.ui.viewModels

sealed interface BaseUiState<out T> {
    data object Loading : BaseUiState<Nothing>
    data object Error : BaseUiState<Nothing>
    data class Success<T>(val data: T) : BaseUiState<T>
}
