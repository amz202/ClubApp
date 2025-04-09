package com.example.clubapp.data.respositories

import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.ClubEventsRequest
import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.ClubMemberResponse
import com.example.clubapp.network.response.ClubResponse
import okhttp3.ResponseBody

interface ClubRepository {
    suspend fun getClub(id:String): ClubRequest
    suspend fun getClubs():List<ClubRequest>
    suspend fun createClub(token:String, club: ClubRequest): ClubResponse
    suspend fun deleteClub(token:String, id: String): ResponseBody
    suspend fun getClubsMembers(token: String, clubId: String): List<ClubMemberResponse>
    suspend fun joinClub(token: String, clubId: String): ResponseBody
    suspend fun leaveClub(token: String, clubId: String): ResponseBody
    suspend fun getUsersClubs(token: String, userId: String): List<ClubMemberResponse>
    suspend fun changeClubMemberRole(token: String, clubId: String, userId: String, request: RoleRequest): ResponseBody
}

class ClubRepositoryImpl(private val apiService: ApiService): ClubRepository{
    override suspend fun getClub(id: String): ClubRequest = apiService.getClub(id)

    override suspend fun getClubs(): List<ClubRequest> = apiService.getClubs()

    override suspend fun createClub(token: String, club: ClubRequest): ClubResponse = apiService.createClub(token = "Bearer $token", club = club)

    override suspend fun deleteClub(token: String, id: String): ResponseBody = apiService.deleteClub(token = "Bearer $token", id = id)

    override suspend fun getClubsMembers(token: String, clubId: String): List<ClubMemberResponse> {
        return apiService.getClubsMembers(token = "Bearer $token", clubId = clubId)
    }

    override suspend fun joinClub(token: String, clubId: String): ResponseBody {
        return apiService.joinClub(token = "Bearer $token", clubId = clubId)
    }

    override suspend fun leaveClub(token: String, clubId: String): ResponseBody {
        return apiService.leaveClub(token = "Bearer $token", clubId = clubId)
    }

    override suspend fun getUsersClubs(token: String, userId: String): List<ClubMemberResponse> {
        return apiService.getUsersClubs(token = "Bearer $token", userId = userId)
    }

    override suspend fun changeClubMemberRole(token: String, clubId: String, userId: String, request: RoleRequest): ResponseBody {
        return apiService.changeClubMemberRole(token = "Bearer $token", clubId = clubId, request = request)
    }
}