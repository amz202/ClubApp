package com.example.clubapp.data.respositories

import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.ClubMembersResponse
import com.example.clubapp.network.response.ClubResponse
import okhttp3.ResponseBody

interface ClubRepository {
    suspend fun getClub(id:String): ClubResponse
    suspend fun getClubs():List<ClubResponse>
    suspend fun createClub(token:String, club: ClubRequest): ResponseBody
    suspend fun deleteClub(token:String, id: String): ResponseBody
    suspend fun getClubsMembers(token: String, clubId: String): List<ClubMembersResponse>
    suspend fun joinClub(token: String, clubId: String): ResponseBody
    suspend fun leaveClub(token: String, clubId: String): ResponseBody
    suspend fun getUsersClubs(token: String, userId: String): List<ClubMembersResponse>
    suspend fun changeClubMemberRole(token: String, clubId: String, userId: String, request: RoleRequest): ResponseBody
    suspend fun getClubRole(token:String, clubId:String, userId:String): String?
    suspend fun getMyClubs(token: String):List<ClubResponse>?
}

class ClubRepositoryImpl(private val apiService: ApiService): ClubRepository{
    override suspend fun getClub(id: String): ClubResponse = apiService.getClub(id)

    override suspend fun getClubs(): List<ClubResponse> = apiService.getClubs()

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
        return apiService.changeClubMemberRole(token = "Bearer $token", clubId = clubId, request = request)
    }

    override suspend fun getClubRole(token: String, clubId: String, userId: String): String? {
        return apiService.getClubRole(token = "Bearer $token", clubId = clubId, userId = userId)
    }

    override suspend fun getMyClubs(token: String): List<ClubResponse>? {
        return apiService.getMyClubs(token= "Bearer $token")
    }
}