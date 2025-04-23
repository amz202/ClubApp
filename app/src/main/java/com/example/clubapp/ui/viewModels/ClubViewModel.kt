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
import com.example.clubapp.network.response.EventParticipantsResponse
import com.example.clubapp.network.response.EventResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.clubapp.network.response.RoleResponse
import kotlin.collections.containsKey
import kotlin.text.get

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

    private val _userClubRole = MutableStateFlow<RoleResponse?>(null)
    val userClubRole: StateFlow<RoleResponse?> = _userClubRole

    private val clubCache = mutableMapOf<String, ClubResponse>()
    private val membersCache = mutableMapOf<String, List<ClubMembersResponse>>()
    private val userClubsCache = mutableMapOf<String, List<ClubResponse>>()
    private val userClubRoleCache = mutableMapOf<String, RoleResponse?>()

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
                val cacheKey = token
                if (userClubsCache.containsKey(cacheKey)) {
                    _usersClub.value = userClubsCache[cacheKey]
                    return@launch
                }
                val myClubs = clubRepository.getMyClubs(token.toString())
                if(!myClubs.isNullOrEmpty()){
                    _usersClub.value = myClubs
                    userClubsCache[cacheKey] = myClubs
                }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getClub(id:String){
        viewModelScope.launch {
            if(clubCache.containsKey(id)){
                _clubOfId.value = clubCache[id]
                return@launch
            }
            _clubOfId.value = null
            try {
                val club = clubRepository.getClub(id)
                _clubOfId.value = club
                clubCache[id] = club
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
            if (membersCache.containsKey(clubId)) {
                _clubMembers.value = membersCache[clubId] ?:emptyList()
                return@launch
            }
            _clubMembers.value = emptyList()
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    _clubMembers.value = emptyList() // Clear members if token is null
                    return@launch
                }
                val members = clubRepository.getClubsMembers(token, clubId)
                _clubMembers.value = members // Update the StateFlow with the new members
                membersCache[clubId] = members // Cache the members
            } catch (e: Exception) {
                e.printStackTrace()
                _clubMembers.value = emptyList() // Clear members on error
            }
        }
    }

    fun changeClubMemberRole(clubId: String, userId: String, request: RoleRequest, ownRole: String) {
        viewModelScope.launch {
            clubMemberUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.changeClubMemberRole(token, clubId=clubId, userId=userId, request, ownRole)
                membersCache.remove(clubId)
                getClubMembers(clubId)
                val updatedMembers = clubRepository.getClubsMembers(token, clubId)
                clubMemberUiState = BaseUiState.Success(updatedMembers)
            } catch (e: Exception) {
                clubMemberUiState = BaseUiState.Error
            }
        }
    }

    fun getClubRole(clubId: String){
        viewModelScope.launch {
            if(userClubRoleCache.containsKey(clubId)) {
                _userClubRole.value = userClubRoleCache[clubId]
                return@launch
            }
            _userClubRole.value = null
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                val role = clubRepository.getClubRole(token, clubId)
                _userClubRole.value = role
                userClubRoleCache[clubId] = role
            } catch (e: Exception) {
                _userClubRole.value = null
                e.printStackTrace()
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