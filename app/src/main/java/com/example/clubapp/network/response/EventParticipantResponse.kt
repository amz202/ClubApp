package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class EventParticipantResponse(
    val userId: String,
    val eventId: String,
    val eventRole: String,
    val joinedOn: String,
)