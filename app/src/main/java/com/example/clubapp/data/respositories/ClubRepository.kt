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
    suspend fun createClub(club: ClubRequest): ClubResponse
    suspend fun deleteClub(id: String): ResponseBody
    suspend fun getClubEvents(clubEventsRequest: ClubEventsRequest): List<EventRequest>
}

class ClubRepositoryImpl(private val apiService: ApiService): ClubRepository{
    override suspend fun getClub(id: String): ClubRequest = apiService.getClub(id)

    override suspend fun getClubs(): List<ClubRequest> = apiService.getClubs()

    override suspend fun createClub(club: ClubRequest): ClubResponse = apiService.createClub(club)

    override suspend fun deleteClub(id: String): ResponseBody = apiService.deleteClub(id)

    override suspend fun getClubEvents(clubEventsRequest: ClubEventsRequest): List<EventRequest> = apiService.getClubEvents(clubEventsRequest)
}