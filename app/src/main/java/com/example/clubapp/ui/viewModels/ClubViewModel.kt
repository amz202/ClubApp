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
import com.example.clubapp.network.response.ClubMembersResponse
import com.example.clubapp.network.response.ClubResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

typealias ClubUiState = BaseUiState<List<ClubResponse>>
typealias ClubMemberUiState = BaseUiState<List<ClubMembersResponse>>
typealias ClubRoleUiState = BaseUiState<String?>

class ClubViewModel(
    private val clubRepository: ClubRepository,
    private val userPreferences: UserPreferences
): ViewModel() {
    var uiState: ClubUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var clubMemberUiState: ClubMemberUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var clubRoleUiState: ClubRoleUiState by mutableStateOf(BaseUiState.Loading)
        private set

    private val _usersClub = MutableStateFlow<List<ClubResponse>?>(emptyList())
    val usersClub: StateFlow<List<ClubResponse>?> = _usersClub

    private val _clubs = MutableStateFlow<List<ClubResponse>>(emptyList())
    val clubs: StateFlow<List<ClubResponse>> = _clubs

    private val _clubMembers = MutableStateFlow<List<ClubMembersResponse>>(emptyList())
    val clubMembers: StateFlow<List<ClubMembersResponse>> = _clubMembers

    private val _clubOfId = MutableStateFlow<ClubResponse?>(null)
    val clubOfId: StateFlow<ClubResponse?> = _clubOfId

    init{
        getClubs()
    }

    fun getClubs() {
        viewModelScope.launch {
            try {
                uiState = BaseUiState.Loading
                val clubs = clubRepository.getClubs()
                if (clubs.isEmpty()) {
                    uiState = BaseUiState.Error
                } else {
                    uiState = BaseUiState.Success(clubs)
                    _clubs.value = clubs
                }
            } catch (e: Exception) {
                uiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getMyClubs(){
        viewModelScope.launch {
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    return@launch
                }
                val myClubs = clubRepository.getMyClubs(token.toString())
                if(!myClubs.isNullOrEmpty()){
                    _usersClub.value = myClubs
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getClub(id:String){
        viewModelScope.launch {
            try {
                val club = clubRepository.getClub(id)
                _clubOfId.value = club
            } catch (e: Exception) {
                _clubOfId.value = null
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
                val updatedClubs = clubRepository.getClubs()  //as a result, there is a GET request right after the POST request in event logs
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

    fun getClubMembers(clubId: String) {
        viewModelScope.launch {
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    _clubMembers.value = emptyList() // Clear members if token is null
                    return@launch
                }
                val members = clubRepository.getClubsMembers(token, clubId)
                _clubMembers.value = members // Update the StateFlow with the new members
            } catch (e: Exception) {
                e.printStackTrace()
                _clubMembers.value = emptyList() // Clear members on error
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

    fun getClubRole(clubId: String, userId: String){
        viewModelScope.launch {
            clubRoleUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                val role = clubRepository.getClubRole(token, clubId, userId)
                clubRoleUiState = BaseUiState.Success(role)
            } catch (e: Exception) {
                clubRoleUiState = BaseUiState.Error
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