package com.example.clubapp.data

import com.example.clubapp.data.respositories.ChatRepository
import com.example.clubapp.data.respositories.ChatRepositoryImpl
import com.example.clubapp.data.respositories.ChatWSRepository
import com.example.clubapp.data.respositories.ChatWSRepositoryImpl
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
    val chatWSRepository: ChatWSRepository
    val chatRepository: ChatRepository
}

class DefaultAppContainer:AppContainer{
    val BASE_URL = "http://192.168.208.28:8001/"
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
    override val chatRepository: ChatRepository by lazy {
        ChatRepositoryImpl(apiService)
    }
    override val chatWSRepository: ChatWSRepository by lazy {
        ChatWSRepositoryImpl()
    }
}