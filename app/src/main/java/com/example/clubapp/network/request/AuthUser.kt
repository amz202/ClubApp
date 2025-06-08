package com.example.clubapp.network.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val photoUrl : String
)
