package com.example.clubapp.data.respositories

import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.ClubEventsRequest
import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.response.ClubResponse
import okhttp3.ResponseBody

interface ClubRepository {
    suspend fun getClub(id:String): ClubRequest
    suspend fun getClubs():List<ClubRequest>
    suspend fun createClub(token:String, club: ClubRequest): ClubResponse
    suspend fun deleteClub(token:String, id: String): ResponseBody
}

class ClubRepositoryImpl(private val apiService: ApiService): ClubRepository{
    override suspend fun getClub(id: String): ClubRequest = apiService.getClub(id)

    override suspend fun getClubs(): List<ClubRequest> = apiService.getClubs()

    override suspend fun createClub(token: String, club: ClubRequest): ClubResponse = apiService.createClub(token = "Bearer $token", club = club)

    override suspend fun deleteClub(token: String, id: String): ResponseBody = apiService.deleteClub(token = "Bearer $token", id = id)
}