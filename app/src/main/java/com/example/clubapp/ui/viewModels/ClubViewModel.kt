package com.example.clubapp.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clubapp.ClubApplication
import com.example.clubapp.data.respositories.ClubRepository
import com.example.clubapp.network.request.ClubRequest
import kotlinx.coroutines.launch

typealias ClubUiState = BaseUiState<List<ClubRequest>>

class ClubViewModel(private val clubRepository: ClubRepository): ViewModel() {
    var uiState: ClubUiState by mutableStateOf(BaseUiState.Loading)
        private set

    fun getClubs() {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val clubs = clubRepository.getClubs()
                uiState = BaseUiState.Success(clubs)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun getClub(id:String){
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val club = clubRepository.getClub(id)
                uiState = BaseUiState.Success(listOf(club))
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun createClub(token:String, club: ClubRequest){
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                clubRepository.createClub(token, club)
                val updatedClubs = clubRepository.getClubs()
                uiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun deleteClub(token: String, id: String){
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                clubRepository.deleteClub(token, id)
                val updatedClubs = clubRepository.getClubs()
                uiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }
    companion object {
        val clubFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClubApplication
                ClubViewModel(app.container.clubRepository)
            }
        }
    }
}