package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubGroupResponse(
    val id: String,
    val name: String,
    val clubId: String
)