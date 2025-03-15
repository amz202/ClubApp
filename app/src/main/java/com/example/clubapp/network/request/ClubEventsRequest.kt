package com.example.clubapp.network.request

import kotlinx.serialization.Serializable

@Serializable
data class ClubEventsRequest(
    val clubId: String
)
