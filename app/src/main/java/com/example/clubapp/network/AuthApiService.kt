package com.example.clubapp.network

import com.example.clubapp.network.request.AuthUser
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("login") // Matches your Ktor route
    suspend fun login(@Header("Authorization") token: String): AuthUser
}
