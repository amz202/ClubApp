package com.example.clubapp.network.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class ClubResponse(
    val name: String,
    val description: String,
    val id: String,
    val createdBy:String,
    val tags:String,
    val createdOn: String,
    val memberCount: Int,
    val isOpen: Boolean
)
