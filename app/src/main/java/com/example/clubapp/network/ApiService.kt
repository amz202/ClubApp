package com.example.clubapp.network

import com.example.clubapp.network.request.ClubRequest
import com.example.clubapp.network.request.EditMessageRequest
import com.example.clubapp.network.request.EventNewsRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.ChatMessageResponse
import com.example.clubapp.network.response.ClubGroupResponse
import com.example.clubapp.network.response.ClubMembersResponse
import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.network.response.EventNewsResponse
import com.example.clubapp.network.response.EventParticipantsResponse
import com.example.clubapp.network.response.EventResponse
import com.example.clubapp.network.response.RoleResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

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

interface ApiService {
    @GET("clubs/{id}")
    suspend fun getClub(@Path("id") id: String): ClubResponse //@Path is used when the variable is part of the url path itself

    @GET("clubs")
    suspend fun getClubs(): List<ClubResponse>

    @POST("clubs")
    suspend fun createClub(
        @Header("Authorization") token: String,
        @Body club: ClubRequest
    ): ResponseBody

    @DELETE("clubs/{id}")
    suspend fun deleteClub(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ResponseBody //Raw message from server

    @GET("/clubs/{id}/events")
    suspend fun getClubEvents(@Path("id") id: String): List<EventResponse>?

    @GET("events")
    suspend fun getEvents(): List<EventResponse>

    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: String): EventResponse

    @POST("events")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Body event: EventRequest
    ): ResponseBody

    @DELETE("events/{id}")
    suspend fun deleteEvent(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ResponseBody

    @GET("club/{clubId}/members")
    suspend fun getClubsMembers(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String
    ): List<ClubMembersResponse>

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
    ): List<ClubMembersResponse>

    @POST("club/{clubId}/{userId}/change-role")
    suspend fun changeClubMemberRole(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String,
        @Path("userId") userId: String,
        @Body request: RoleRequest,
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
    ): List<EventParticipantsResponse>

    @GET("user/{userId}/events")
    suspend fun getUserEvents(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): List<EventParticipantsResponse>

    @GET("user/clubs")
    suspend fun getMyClubs(
        @Header("Authorization") token: String
    ): List<ClubResponse>?

    @GET("user/events")
    suspend fun getMyEvents(
        @Header("Authorization") token: String
    ): List<EventResponse>?

    @POST("/events/{eventId}/{userId}/change-role")
    suspend fun changeEventParticipantRole(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String,
        @Path("userId") userId: String,
        @Body request: RoleRequest,
    ): ResponseBody

    @GET("/club/{clubId}/role")
    suspend fun getClubRole(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String
    ): RoleResponse?

    @GET("/events/{eventId}/role")
    suspend fun getEventRole(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String,
    ): RoleResponse?

    @POST("/events/{eventId}/news")
    suspend fun createEventNews(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String,
        @Body news: EventNewsRequest
    ): ResponseBody

    @GET("events/{eventId}/news")
    suspend fun getEventNews(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): List<EventNewsResponse>

    @GET("/events/news/{eventNewsId}")
    suspend fun getEventNewsById(
        @Header("Authorization") token: String,
        @Path("eventNewsId") eventNewsId: String
    ): EventNewsResponse

    @GET("/{clubId}/group")
    suspend fun getClubGroup(
        @Header("Authorization") token: String,
        @Path("clubId") clubId: String
    ): ClubGroupResponse?

    @GET("/edit")
    suspend fun editMessage(
        @Header("Authorization") token: String,
        @Body body: EditMessageRequest
    ): ResponseBody

    @DELETE("/{id}/delete")
    suspend fun deleteMessage(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ResponseBody

    @GET("/{groupId}/recentChat")
    suspend fun recentChat(
        @Header("Authorization") token: String,
        @Path("groupId") groupId: String
    ): List<ChatMessageResponse>?

}

