package com.example.clubapp.data

import com.example.clubapp.data.respositories.ClubRepository
import com.example.clubapp.data.respositories.ClubRepositoryImpl
import com.example.clubapp.data.respositories.EventRepository
import com.example.clubapp.data.respositories.EventRepositoryImpl
import com.example.clubapp.network.ApiService
import com.example.clubapp.network.AuthApiService
import com.example.clubapp.signin.AuthRepository
import com.example.clubapp.signin.AuthRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer{
    val authRepository:AuthRepository
    val eventRepository:EventRepository
    val clubRepository: ClubRepository
}

class DefaultAppContainer:AppContainer{
    val BASE_URL = "http://192.168.18.67:5432/"
    val json = Json{
        this.ignoreUnknownKeys = true
        coerceInputValues = true
    }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    val authService: AuthApiService by lazy{
        retrofit.create(AuthApiService::class.java)
    }
    val apiService: ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }
    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authService)
    }
    override val eventRepository: EventRepository by lazy {
        EventRepositoryImpl(apiService)
    }
    override val clubRepository: ClubRepository by lazy {
        ClubRepositoryImpl(apiService)
    }
}