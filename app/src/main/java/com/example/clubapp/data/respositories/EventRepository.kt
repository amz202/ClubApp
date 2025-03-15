package com.example.clubapp.data.respositories

import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.response.EventResponse
import okhttp3.ResponseBody

interface EventRepository {
    suspend fun getEvent(id:String): EventRequest
    suspend fun getEvents():List<EventRequest>
    suspend fun createEvent(event: EventRequest): EventResponse
    suspend fun deleteEvent(id: String): ResponseBody
}

class EventRepositoryImpl(private val apiService: ApiService): EventRepository{
    override suspend fun getEvent(id: String): EventRequest = apiService.getEvent(id)

    override suspend fun getEvents(): List<EventRequest> = apiService.getEvents()

    override suspend fun createEvent(event: EventRequest): EventResponse = apiService.createEvent(event)

    override suspend fun deleteEvent(id: String): ResponseBody = apiService.deleteEvent(id)
}