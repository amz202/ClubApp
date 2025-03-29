package com.example.clubapp.data.respositories

import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.ClubEventsRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.response.EventResponse
import okhttp3.ResponseBody

interface EventRepository {
    suspend fun getEvent(id:String): EventRequest
    suspend fun getEvents():List<EventRequest>
    suspend fun createEvent(token:String, event: EventRequest): EventResponse
    suspend fun deleteEvent(token:String, id: String): ResponseBody
    suspend fun getClubEvents(clubEventsRequest: ClubEventsRequest): List<EventRequest>

}

class EventRepositoryImpl(private val apiService: ApiService): EventRepository{
    override suspend fun getEvent(id: String): EventRequest = apiService.getEvent(id)

    override suspend fun getEvents(): List<EventRequest> = apiService.getEvents()

    override suspend fun createEvent(token:String, event: EventRequest): EventResponse = apiService.createEvent(token = "Bearer $token", event = event)

    override suspend fun deleteEvent(token:String, id: String): ResponseBody = apiService.deleteEvent(token = "Bearer $token", id = id)

    override suspend fun getClubEvents(clubEventsRequest: ClubEventsRequest): List<EventRequest> = apiService.getClubEvents(clubEventsRequest)

}