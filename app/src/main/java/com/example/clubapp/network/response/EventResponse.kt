package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val name: String,
    val description: String,
    val clubId: String?,
    val dateTime : String,
    val location: String,
    val capacity: String?,
    val organizedBy:String,
    val id:String
)