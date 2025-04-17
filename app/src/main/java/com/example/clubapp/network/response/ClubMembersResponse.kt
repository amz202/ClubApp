package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ClubMembersResponse(
    val id: String,
    val email: String,
    val name: String,
    val clubRole: String
)