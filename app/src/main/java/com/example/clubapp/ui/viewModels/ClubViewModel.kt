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
import com.example.clubapp.network.response.ClubGroupResponse
import com.example.clubapp.network.response.ClubJoinResponse
import com.example.clubapp.network.response.RoleResponse
import kotlin.collections.containsKey
import kotlin.collections.remove
import kotlin.text.get
import kotlin.text.set
import kotlin.toString
import kotlinx.coroutines.flow.first

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

typealias ClubUiState = BaseUiState<List<ClubResponse>>
typealias ClubMemberUiState = BaseUiState<List<ClubMembersResponse>>
typealias ClubRoleUiState = BaseUiState<String?>
typealias UserClubsUiState = BaseUiState<List<ClubResponse>>
typealias SingleClubUiState = BaseUiState<ClubResponse>
typealias ClubActionUiState = BaseUiState<Boolean>
typealias ClubGroupUiState = BaseUiState<ClubGroupResponse>
typealias ClubPendingMemberUiState = BaseUiState<List<ClubJoinResponse>>

class ClubViewModel(
    private val clubRepository: ClubRepository,
    private val userPreferences: UserPreferences
): ViewModel() {
    var uiState: ClubUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var clubMemberUiState: ClubMemberUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var clubPendingMemberUiState: ClubPendingMemberUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var clubRoleUiState: ClubRoleUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var userClubsUiState: UserClubsUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var singleClubUiState: SingleClubUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var joinClubUiState: ClubActionUiState by mutableStateOf(BaseUiState.Success(false))
        private set

    var leaveClubUiState: ClubActionUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var clubGroupUiState: ClubGroupUiState by mutableStateOf(BaseUiState.Loading)
        private set

    private val _usersClub = MutableStateFlow<List<ClubResponse>?>(emptyList())
    val usersClub: StateFlow<List<ClubResponse>?> = _usersClub

    private val _clubs = MutableStateFlow<List<ClubResponse>>(emptyList())
    val clubs: StateFlow<List<ClubResponse>> = _clubs

    private val _clubMembers = MutableStateFlow<List<ClubMembersResponse>>(emptyList())
    val clubMembers: StateFlow<List<ClubMembersResponse>> = _clubMembers

    private val _clubOfId = MutableStateFlow<ClubResponse?>(null)
    val clubOfId: StateFlow<ClubResponse?> = _clubOfId

    private val _clubGroup = MutableStateFlow<ClubGroupResponse?>(null)
    val clubGroup: StateFlow<ClubGroupResponse?> = _clubGroup

    private val _userClubRole = MutableStateFlow<RoleResponse?>(null)
    val userClubRole: StateFlow<RoleResponse?> = _userClubRole

    private val _clubStatus = MutableStateFlow<String>("")
    val clubStatus: StateFlow<String> = _clubStatus

    private val _pendingMembers = MutableStateFlow<List<ClubJoinResponse>?>(emptyList())
    val pendingMembers: StateFlow<List<ClubJoinResponse>?> = _pendingMembers

    private val clubCache = mutableMapOf<String, ClubResponse>()
    private val membersCache = mutableMapOf<String, List<ClubMembersResponse>>()
    private val userClubsCache = mutableMapOf<String, List<ClubResponse>>()
    private val userClubRoleCache = mutableMapOf<String, RoleResponse?>()
    private val clubGroupCache = mutableMapOf<String, ClubGroupResponse?>()

    init{
        getClubs()
    }

    fun getClubs() {
        viewModelScope.launch {
            try {
                uiState = BaseUiState.Loading
                clubRepository.getClubs().collect { clubs ->
                    uiState = BaseUiState.Success(clubs)
                    _clubs.value = clubs
                    Log.d("ClubViewModel", "Updated clubs in ViewModel: ${clubs.size}")
                }
            } catch (e: Exception) {
                uiState = BaseUiState.Error
                Log.e("ClubViewModel", "Error collecting clubs flow", e)
                e.printStackTrace()
            }
        }
    }

    fun refreshClubs() {
        viewModelScope.launch {
            try {
                clubRepository.refreshClubs()
                // After refreshing, the flow collector in getClubs() will automatically update the UI state
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMyClubs(){
        viewModelScope.launch {
            try {
                userClubsUiState = BaseUiState.Loading
                val token = userPreferences.getToken()
                if (token == null) {
                    userClubsUiState = BaseUiState.Error
                    return@launch
                }
                val cacheKey = token
                if (userClubsCache.containsKey(cacheKey)) {
                    _usersClub.value = userClubsCache[cacheKey]
                    userClubsUiState = BaseUiState.Success(userClubsCache[cacheKey] ?: emptyList())
                    return@launch
                }
                val myClubs = clubRepository.getMyClubs(token.toString())
                if(!myClubs.isNullOrEmpty()){
                    _usersClub.value = myClubs
                    userClubsCache[cacheKey] = myClubs
                    userClubsUiState = BaseUiState.Success(myClubs)
                } else {
                    userClubsUiState = BaseUiState.Success(emptyList())
                }
            } catch (e: Exception) {
                userClubsUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getClub(id:String){
        viewModelScope.launch {
            singleClubUiState = BaseUiState.Loading
            if(clubCache.containsKey(id)){
                _clubOfId.value = clubCache[id]
                singleClubUiState = BaseUiState.Success(clubCache[id]!!)
                return@launch
            }
            _clubOfId.value = null
            try {
                val club = clubRepository.getClub(id)
                _clubOfId.value = club
                clubCache[id] = club
                _clubStatus.value = club.status
                singleClubUiState = BaseUiState.Success(club)
            } catch (e: Exception) {
                _clubOfId.value = null
                singleClubUiState = BaseUiState.Error
                e.printStackTrace()
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
                val updatedClubs = clubRepository.getClubs().first()
                uiState = BaseUiState.Success(updatedClubs)
                _clubs.value = updatedClubs
                uiState = BaseUiState.Success(updatedClubs)
                val cacheKey = token
                val myClubs = clubRepository.getMyClubs(token.toString())
                if(!myClubs.isNullOrEmpty()){
                    _usersClub.value = myClubs
                    userClubsCache[cacheKey] = myClubs
                    userClubsUiState = BaseUiState.Success(myClubs)
                } else {
                    userClubsUiState = BaseUiState.Success(emptyList())
                }
            } catch (e: Exception) {
                uiState = BaseUiState.Error
                e.printStackTrace()
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
                val updatedClubs = clubRepository.getClubs().first()
                uiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun joinClub(clubId: String){
        viewModelScope.launch {
            joinClubUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    joinClubUiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.joinClub(token, clubId)

                // Clear relevant caches to ensure fresh data
                userClubRoleCache.remove(clubId)
                userClubsCache.remove(token)

                val updatedRole = clubRepository.getClubRole(token, clubId)
                _userClubRole.value = updatedRole
                userClubRoleCache[clubId] = updatedRole
                clubRoleUiState = BaseUiState.Success(updatedRole?.role)

                val myClubs = clubRepository.getMyClubs(token.toString())
                if (!myClubs.isNullOrEmpty()) {
                    _usersClub.value = myClubs
                    userClubsCache[token] = myClubs
                    userClubsUiState = BaseUiState.Success(myClubs)
                } else {
                    _usersClub.value = emptyList()
                    userClubsUiState = BaseUiState.Success(emptyList())
                }
                joinClubUiState = BaseUiState.Success(true)
            } catch (e: Exception) {
                joinClubUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun leaveClub(clubId: String){
        viewModelScope.launch {
            leaveClubUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    leaveClubUiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.leaveClub(token, clubId)
                val updatedClubs = clubRepository.getClubs().first()
                _clubs.value = updatedClubs
                leaveClubUiState = BaseUiState.Success(true)

                // Clear caches to update lists
                val cacheKey = token
                userClubsCache.remove(cacheKey)
                membersCache.remove(clubId)
                userClubRoleCache.remove(clubId)

                // Update UI state
                uiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                leaveClubUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getClubMembers(clubId: String) {
        viewModelScope.launch {
            clubMemberUiState = BaseUiState.Loading
            if (membersCache.containsKey(clubId)) {
                _clubMembers.value = membersCache[clubId] ?: emptyList()
                clubMemberUiState = BaseUiState.Success(membersCache[clubId] ?: emptyList())
                return@launch
            }
            _clubMembers.value = emptyList()
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    _clubMembers.value = emptyList()
                    clubMemberUiState = BaseUiState.Error
                    return@launch
                }
                val members = clubRepository.getClubsMembers(token, clubId)
                _clubMembers.value = members
                membersCache[clubId] = members
                clubMemberUiState = BaseUiState.Success(members)
            } catch (e: Exception) {
                e.printStackTrace()
                _clubMembers.value = emptyList()
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
                    clubMemberUiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.changeClubMemberRole(token, clubId=clubId, userId=userId, request)
                membersCache.remove(clubId)
                getClubMembers(clubId)
                val updatedMembers = clubRepository.getClubsMembers(token, clubId)
                clubMemberUiState = BaseUiState.Success(updatedMembers)
            } catch (e: Exception) {
                clubMemberUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getClubRole(clubId: String){
        viewModelScope.launch {
            clubRoleUiState = BaseUiState.Loading
            if(userClubRoleCache.containsKey(clubId)) {
                _userClubRole.value = userClubRoleCache[clubId]
                clubRoleUiState = BaseUiState.Success(userClubRoleCache[clubId]?.role)
                return@launch
            }
            _userClubRole.value = null
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    clubRoleUiState = BaseUiState.Error
                    return@launch
                }
                val role = clubRepository.getClubRole(token, clubId)
                _userClubRole.value = role
                userClubRoleCache[clubId] = role
                clubRoleUiState = BaseUiState.Success(role?.role)
            } catch (e: Exception) {
                _userClubRole.value = null
                clubRoleUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getClubGroup(clubId: String) {
        viewModelScope.launch {
            clubGroupUiState = BaseUiState.Loading
            if(clubGroupCache.containsKey(clubId)){
                _clubGroup.value = clubGroupCache[clubId]
                clubGroupUiState = BaseUiState.Success(clubGroupCache[clubId]!!)
                return@launch
            }
            if (_clubGroup.value != null && _clubGroup.value?.id == clubId) {
                clubGroupUiState = BaseUiState.Success(_clubGroup.value!!)
                return@launch
            }
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    clubGroupUiState = BaseUiState.Error
                    return@launch
                }
                val group = clubRepository.getClubGroup(token, clubId)
                if (group != null) {
                    _clubGroup.value = group
                    clubGroupCache[clubId] = group
                    clubGroupUiState = BaseUiState.Success(group)
                } else {
                    clubGroupUiState = BaseUiState.Error
                }
            } catch (e: Exception) {
                _clubGroup.value = null
                clubGroupUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun openClub(id: String){
        viewModelScope.launch {
            singleClubUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.openCLub(token, id)
                val updatedClubs = clubRepository.getClub(id)
                _clubStatus.value = updatedClubs.status
                singleClubUiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                singleClubUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun closeClub(id: String){
        viewModelScope.launch {
            singleClubUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.closeClub(token, id)
                val updatedClubs = clubRepository.getClub(id)
                _clubStatus.value = updatedClubs.status
                singleClubUiState = BaseUiState.Success(updatedClubs)
            } catch (e: Exception) {
                singleClubUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getPendingMembers(id: String){
        viewModelScope.launch {
            clubPendingMemberUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    clubPendingMemberUiState = BaseUiState.Error
                    return@launch
                }
                val pendingMembers = clubRepository.getPendingMembers(token, id)
                if(!pendingMembers.isNullOrEmpty()){
                    _pendingMembers.value = pendingMembers
                    clubPendingMemberUiState = BaseUiState.Success(emptyList())
                } else {
                    _pendingMembers.value = emptyList()
                    clubPendingMemberUiState = BaseUiState.Success(emptyList())
                }
            } catch (e: Exception) {
                clubPendingMemberUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun approveMember(clubId: String, userId: String){
        viewModelScope.launch {
            clubPendingMemberUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    clubPendingMemberUiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.approveMember(token, clubId, userId)
                val updatedRequests = clubRepository.getPendingMembers(token, clubId)
                _pendingMembers.value = updatedRequests
                membersCache.clear()
                clubPendingMemberUiState = if(!updatedRequests.isNullOrEmpty()){
                    BaseUiState.Success(updatedRequests)
                } else {
                    BaseUiState.Success(emptyList())
                }
            } catch (e: Exception) {
                clubPendingMemberUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun rejectMember(clubId: String, userId: String){
        viewModelScope.launch {
            clubPendingMemberUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    clubPendingMemberUiState = BaseUiState.Error
                    return@launch
                }
                clubRepository.rejectMember(token, clubId, userId)
                val updatedMembers = clubRepository.getPendingMembers(token, clubId)
                _pendingMembers.value = updatedMembers
                clubPendingMemberUiState = if(!updatedMembers.isNullOrEmpty()){
                    BaseUiState.Success(updatedMembers)
                } else {
                    BaseUiState.Success(emptyList())
                }
            } catch (e: Exception) {
                clubPendingMemberUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }


    fun clearClubsState(){
        clubCache.clear()
        userClubRoleCache.clear()
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