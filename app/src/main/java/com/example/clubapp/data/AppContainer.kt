package com.example.clubapp.data

import android.content.Context
import androidx.room.Room
import com.example.clubapp.data.local.ClubAppDatabase
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

class DefaultAppContainer(private val context: Context):AppContainer{
    private val BASE_URL = "http://10.254.134.160:8001/"
    private val json = Json{
        this.ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val authService: AuthApiService by lazy{
        retrofit.create(AuthApiService::class.java)
    }
    private val apiService: ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }
    private val database: ClubAppDatabase by lazy {
        Room.databaseBuilder(
            context,
            ClubAppDatabase::class.java,
            "clubapp_database"
        ).build()
    }

    private val eventDao by lazy {
        database.eventDao()
    }
    private val clubDao by lazy {
        database.clubDao()
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authService)
    }
    override val eventRepository: EventRepository by lazy {
        EventRepositoryImpl(apiService, eventDao)
    }
    override val clubRepository: ClubRepository by lazy {
        ClubRepositoryImpl(apiService, clubDao)
    }
    override val chatRepository: ChatRepository by lazy {
        ChatRepositoryImpl(apiService)
    }
    override val chatWSRepository: ChatWSRepository by lazy {
        ChatWSRepositoryImpl()
    }
}