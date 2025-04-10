package com.example.clubapp.network

import com.example.clubapp.network.request.ClubEventsRequest
import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.ClubMemberResponse
import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.network.response.EventParticipantResponse
import com.example.clubapp.network.response.EventResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("clubs/{id}")
    suspend fun getClub(@Path("id") id: String): ClubRequest //@Path is used when the variable is part of the url path itself

    @GET("clubs")
    suspend fun getClubs(): List<ClubRequest>

    @POST("clubs")
    suspend fun createClub(
        @Header("Authorization") token: String,
        @Body club: ClubRequest
    ): ClubResponse

    @DELETE("clubs/{id}")
    suspend fun deleteClub(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ResponseBody //Raw message from server

    @GET("clubs/events")
    suspend fun getClubEvents(@Body clubEventsRequest: ClubEventsRequest): List<EventRequest>

    @GET("events")
    suspend fun getEvents(): List<EventRequest>

    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: String): EventRequest

    @POST("events")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Body event: EventRequest
    ): EventResponse

    @DELETE("events/{id}")
    suspend fun deleteEvent(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ResponseBody

    @GET("club/{clubId}/members")
    suspend fun getClubsMembers(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String
    ): List<ClubMemberResponse>

    @POST("club/{clubId}/join")
    suspend fun joinClub(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String
    ): ResponseBody

    @POST("club/{clubId}/leave")
    suspend fun leaveClub(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String
    ): ResponseBody

    @GET("user/{userId}/clubs")
    suspend fun getUsersClubs(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): List<ClubMemberResponse>

    @POST("club/{clubId}/user/{userId}/change-role")
    suspend fun changeClubMemberRole(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String,
        @Body request: RoleRequest
    ): ResponseBody

    @POST("events/{eventId}/join")
    suspend fun joinEvent(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): ResponseBody

    @POST("events/{eventId}/leave")
    suspend fun leaveEvent(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): ResponseBody

    @GET("events/{eventId}/participants")
    suspend fun getEventParticipants(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): List<EventParticipantResponse>

    @GET("user/{userId}/events")
    suspend fun getUserEvents(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): List<EventParticipantResponse>

    @POST("events/{eventId}/change-role")
    suspend fun changeEventRole(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String,
        @Body request: RoleRequest
    ): ResponseBody

    @GET("/club/{clubId}/user/{userId}/role")
    suspend fun getClubRole(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String,
        @Path("userId") userId: String
    ): String?

    @GET("/events/{eventId}/user/{userId}/role")
    suspend fun getEventRole(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String,
        @Path("userId") userId: String
    ): String?
}

