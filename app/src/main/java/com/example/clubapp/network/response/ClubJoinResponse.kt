package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubJoinResponse(
    val clubId: String,
    val userId: String,
    val status: String,
    val requestedOn: String
)