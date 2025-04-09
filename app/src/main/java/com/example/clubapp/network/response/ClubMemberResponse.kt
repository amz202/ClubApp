package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubMemberResponse(
    val clubId: String,
    val userId: String,
    val clubRole:String,
    val joinedOn : String,
)