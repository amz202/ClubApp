package com.example.clubapp.network.request

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ClubRequest(
    val name: String,
    val description: String,
)
