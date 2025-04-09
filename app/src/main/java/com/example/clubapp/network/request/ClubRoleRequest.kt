package com.example.clubapp.network.request

import kotlinx.serialization.Serializable

@Serializable
data class RoleRequest(
    val role: String
)