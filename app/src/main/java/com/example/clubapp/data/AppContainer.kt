package com.example.clubapp.data

import com.example.clubapp.network.AuthApiService
import com.example.clubapp.signin.AuthRepository
import com.example.clubapp.signin.AuthRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer{
    val authRepository:AuthRepository
}

class DefaultAppContainer:AppContainer{
    val BASE_URL = "http://192.168.208.28:5432/"
    val json = Json{
        this.ignoreUnknownKeys = true
        coerceInputValues = true
    }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    val retrofitService: AuthApiService by lazy{
        retrofit.create(AuthApiService::class.java)
    }
    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(retrofitService)
    }

}