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

interface AppContainer{
    val authRepository:AuthRepository
    val eventRepository:EventRepository
    val clubRepository: ClubRepository
    val chatWSRepository: ChatWSRepository
    val chatRepository: ChatRepository
}

class DefaultAppContainer:AppContainer{
    val BASE_URL = "http://192.168.24.222:8001/"
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