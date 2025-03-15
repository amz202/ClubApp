package com.example.clubapp.network

import com.example.clubapp.network.request.ClubEventsRequest
import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.network.response.EventResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("clubs/{id}")
    suspend fun getClub(@Path("id") id: String): ClubRequest //@Path is used when the variable is part of the url path itself

    @GET("clubs")
    suspend fun getClubs(): List<ClubRequest>

    @POST("clubs")
    suspend fun createClub(@Body club: ClubRequest): ClubResponse

    @DELETE("clubs/{id}")
    suspend fun deleteClub(@Path("id") id: String): ResponseBody //Raw message from server

    @GET("clubs/events")
    suspend fun getClubEvents(@Body clubEventsRequest: ClubEventsRequest): List<EventRequest>

    @GET("events")
    suspend fun getEvents():List<EventRequest>

    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id:String): EventRequest

    @POST("events")
    suspend fun createEvent(@Body event: EventRequest): EventResponse

    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id: String): ResponseBody
}

