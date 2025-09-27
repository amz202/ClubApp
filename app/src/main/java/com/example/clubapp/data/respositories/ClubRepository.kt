package com.example.clubapp.data.respositories

import android.util.Log
import com.example.clubapp.data.local.dao.ClubDao
import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.ClubGroupResponse
import com.example.clubapp.network.response.ClubJoinResponse
import com.example.clubapp.network.response.ClubMembersResponse
import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.network.response.RoleResponse
import com.example.clubapp.data.local.entities.toClubEntity
import com.example.clubapp.data.local.entities.toClubResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.distinctUntilChanged
import okhttp3.ResponseBody

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

interface ClubRepository {
    suspend fun getClub(id:String): ClubResponse
    fun getClubs(): Flow<List<ClubResponse>>
    suspend fun createClub(token:String, club: ClubRequest): ResponseBody
    suspend fun deleteClub(token:String, id: String): ResponseBody
    suspend fun getClubsMembers(token: String, clubId: String): List<ClubMembersResponse>
    suspend fun joinClub(token: String, clubId: String): ResponseBody
    suspend fun leaveClub(token: String, clubId: String): ResponseBody
    suspend fun getUsersClubs(token: String, userId: String): List<ClubMembersResponse>
    suspend fun changeClubMemberRole(token: String, clubId: String, userId: String, request: RoleRequest): ResponseBody
    suspend fun getClubRole(token:String, clubId:String): RoleResponse?
    suspend fun getMyClubs(token: String):List<ClubResponse>?
    suspend fun getClubGroup(token: String, clubId: String): ClubGroupResponse?
    suspend fun openCLub(token: String, id: String): ResponseBody
    suspend fun closeClub(token: String, id: String): ResponseBody
    suspend fun getPendingMembers(token: String, id: String): List<ClubJoinResponse>?
    suspend fun approveMember(token: String, id: String, userId: String): ResponseBody
    suspend fun rejectMember(token: String, id: String, userId: String): ResponseBody
}

class ClubRepositoryImpl(
    private val apiService: ApiService,
    private val clubDao: ClubDao
): ClubRepository{
    override suspend fun getClub(id: String): ClubResponse = apiService.getClub(id)

    override fun getClubs(): Flow<List<ClubResponse>> {
        return flow {
            val cachedClubs = clubDao.getAllClubs().first()
            Log.d("ClubRepository", "Loaded cached clubs: ${cachedClubs.size}")
            emit(cachedClubs.map { it.toClubResponse() })

            try {
                val apiClubs = apiService.getClubs()
                Log.d("ClubRepository", "Fetched clubs from API: ${apiClubs.size}")
                clubDao.insertClubs(apiClubs.map { it.toClubEntity() })
            } catch (e: Exception) {
                Log.e("ClubRepository", "API sync failed, using cached data", e)
            }
        }.distinctUntilChanged()
    }


    override suspend fun createClub(token: String, club: ClubRequest): ResponseBody = apiService.createClub(token = "Bearer $token", club = club)

    override suspend fun deleteClub(token: String, id: String): ResponseBody = apiService.deleteClub(token = "Bearer $token", id = id)

    override suspend fun getClubsMembers(token: String, clubId: String): List<ClubMembersResponse> {
        return apiService.getClubsMembers(token = "Bearer $token", clubId = clubId)
    }

    override suspend fun joinClub(token: String, clubId: String): ResponseBody {
        return apiService.joinClub(token = "Bearer $token", clubId = clubId)
    }

    override suspend fun leaveClub(token: String, clubId: String): ResponseBody {
        return apiService.leaveClub(token = "Bearer $token", clubId = clubId)
    }

    override suspend fun getUsersClubs(token: String, userId: String): List<ClubMembersResponse> {
        return apiService.getUsersClubs(token = "Bearer $token", userId = userId)
    }

    override suspend fun changeClubMemberRole(token: String, clubId: String, userId: String, request: RoleRequest): ResponseBody {
        return apiService.changeClubMemberRole(token = "Bearer $token", clubId = clubId, request = request, userId = userId)
    }

    override suspend fun getClubRole(token: String, clubId: String): RoleResponse? {
        return apiService.getClubRole(token = "Bearer $token", clubId = clubId)
    }

    override suspend fun getMyClubs(token: String): List<ClubResponse>? {
        return apiService.getMyClubs(token= "Bearer $token")
    }

    override suspend fun getClubGroup(token: String, clubId: String): ClubGroupResponse? {
        return apiService.getClubGroup(token = "Bearer $token", clubId = clubId)
    }

    override suspend fun openCLub(token: String, id:String): ResponseBody {
        return apiService.openClub(token = "Bearer $token", id = id)
    }

    override suspend fun closeClub(token: String, id:String): ResponseBody {
        return apiService.closeClub(token = "Bearer $token", id = id)
    }

    override suspend fun getPendingMembers(token: String, id: String): List<ClubJoinResponse>? {
        return apiService.getPendingMembers(token = "Bearer $token", id = id)
    }

    override suspend fun approveMember(token: String, id: String, userId: String): ResponseBody {
        return apiService.approveMember(token = "Bearer $token", id = id, userId = userId)
    }

    override suspend fun rejectMember(token: String, id: String, userId: String): ResponseBody {
        return apiService.rejectMember(token = "Bearer $token", id = id, userId = userId)
    }
}