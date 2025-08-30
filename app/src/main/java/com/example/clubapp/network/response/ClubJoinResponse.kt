package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubJoinResponse(
    val clubId: String,
    val userId: String,
    val name:String,
    val email: String,
    val photoUrl: String,
    val status: String,
    val requestedOn: String
)