package com.example.clubapp.network.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class ClubResponse(
    val name: String
)
