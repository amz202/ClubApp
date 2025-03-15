package com.example.clubapp.signin

import com.example.clubapp.network.AuthApiService
import com.example.clubapp.network.request.AuthUser

interface AuthRepository {
    suspend fun login(token: String): AuthUser
}
class AuthRepositoryImpl(private val apiService: AuthApiService) : AuthRepository {
    override suspend fun login(token: String): AuthUser {
        return apiService.login("Bearer $token") //bearer is a REST API convention
    }
}

//Future Development, attach the token with very route with authenticate function