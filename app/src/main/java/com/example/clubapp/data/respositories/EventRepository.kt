package com.example.clubapp.data.respositories

import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.ClubEventsRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.EventParticipantsResponse
import com.example.clubapp.network.response.EventResponse
import okhttp3.ResponseBody

interface EventRepository {
    suspend fun getEvent(id:String): EventResponse
    suspend fun getEvents():List<EventResponse>
    suspend fun createEvent(token:String, event: EventRequest): ResponseBody
    suspend fun deleteEvent(token:String, id: String): ResponseBody
    suspend fun getClubEvents(clubEventsRequest: ClubEventsRequest): List<EventResponse>
    suspend fun joinEvent(token: String, eventId: String): ResponseBody
    suspend fun leaveEvent(token: String, eventId: String): ResponseBody
    suspend fun getEventParticipants(token: String, eventId: String): List<EventParticipantsResponse>
    suspend fun getUserEvents(token: String, userId: String): List<EventParticipantsResponse>
    suspend fun changeEventRole(token: String, eventId: String, request: RoleRequest): ResponseBody
    suspend fun getEventRole(token: String, eventId: String, userId: String): String?
    suspend fun getMyEvents(token: String): List<EventResponse>?
}

class EventRepositoryImpl(private val apiService: ApiService): EventRepository{
    override suspend fun getEvent(id: String): EventResponse = apiService.getEvent(id)

    override suspend fun getEvents(): List<EventResponse> = apiService.getEvents()

    override suspend fun createEvent(token:String, event: EventRequest): ResponseBody = apiService.createEvent(token = "Bearer $token", event = event)

    override suspend fun deleteEvent(token:String, id: String): ResponseBody = apiService.deleteEvent(token = "Bearer $token", id = id)

    override suspend fun getClubEvents(clubEventsRequest: ClubEventsRequest): List<EventResponse> = apiService.getClubEvents(clubEventsRequest)

    override suspend fun joinEvent(token: String, eventId: String): ResponseBody {
        return apiService.joinEvent(token = "Bearer $token", eventId = eventId)
    }

    override suspend fun leaveEvent(token: String, eventId: String): ResponseBody {
        return apiService.leaveEvent(token = "Bearer $token", eventId = eventId)
    }

    override suspend fun getMyEvents(token: String): List<EventResponse>? {
        return apiService.getMyEvents(token = "Bearer $token")
    }

    override suspend fun getEventParticipants(token: String, eventId: String): List<EventParticipantsResponse> {
        return apiService.getEventParticipants(token = "Bearer $token", eventId = eventId)
    }

    override suspend fun getUserEvents(token: String, userId: String): List<EventParticipantsResponse> {
        return apiService.getUserEvents(token = "Bearer $token", userId = userId)
    }

    override suspend fun changeEventRole(token: String, eventId: String, request: RoleRequest): ResponseBody {
        return apiService.changeEventRole(token = "Bearer $token", eventId = eventId, request = request)
    }

    override suspend fun getEventRole(token: String, eventId: String, userId: String): String? {
        return apiService.getEventRole(token = "Bearer $token", eventId = eventId, userId = userId)
    }
}