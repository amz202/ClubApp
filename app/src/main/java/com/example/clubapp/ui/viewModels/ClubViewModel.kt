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
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.data.respositories.ClubRepository
import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.ClubMemberResponse
import kotlinx.coroutines.launch

typealias ClubUiState = BaseUiState<List<ClubRequest>>
typealias ClubMemberUiState = BaseUiState<List<ClubMemberResponse>>

class ClubViewModel(
    private val clubRepository: ClubRepository,
    private val userPreferences: UserPreferences
): ViewModel() {
    var uiState: ClubUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var clubMemberUiState: ClubMemberUiState by mutableStateOf(BaseUiState.Loading)
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

    fun createClub(club: ClubRequest){
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.createClub(token, club)
                val updatedClubs = clubRepository.getClubs()
                uiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun deleteClub(id: String){
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.deleteClub(token, id)
                val updatedClubs = clubRepository.getClubs()
                uiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun joinClub(clubId: String){
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.joinClub(token, clubId)
                val updatedClubs = clubRepository.getClubs()
                uiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun leaveClub(clubId: String){
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.leaveClub(token, clubId)
                val updatedClubs = clubRepository.getClubs()
                uiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun getClubsMembers(clubId: String) {
        viewModelScope.launch {
            clubMemberUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                val members = clubRepository.getClubsMembers(token, clubId)
                clubMemberUiState = BaseUiState.Success(members)
            } catch (e: Exception) {
                clubMemberUiState = BaseUiState.Error
            }
        }
    }

    fun getUsersClubs(userId: String) {
        viewModelScope.launch {
            clubMemberUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                val members = clubRepository.getUsersClubs(token, userId)
                clubMemberUiState = BaseUiState.Success(members)
            } catch (e: Exception) {
                clubMemberUiState = BaseUiState.Error
            }
        }
    }

    fun changeClubMemberRole(clubId: String, userId: String, request: RoleRequest) {
        viewModelScope.launch {
            clubMemberUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.changeClubMemberRole(token, clubId, userId, request)
                val updatedMembers = clubRepository.getClubsMembers(token, clubId)
                clubMemberUiState = BaseUiState.Success(updatedMembers)
            } catch (e: Exception) {
                clubMemberUiState = BaseUiState.Error
            }
        }
    }
    companion object {
        val clubFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClubApplication
                ClubViewModel(app.container.clubRepository, app.userPreferences)
            }
        }
    }
}
//suspend fun joinClub(token: String, clubId: String): ResponseBody
//suspend fun leaveClub(token: String, clubId: String): ResponseBody
//suspend fun getUsersClubs(token: String, userId: String): List<ClubMemberResponse>
//suspend fun changeClubMemberRole(token: String, clubId: String, userId: String, request: RoleRequest): ResponseBody