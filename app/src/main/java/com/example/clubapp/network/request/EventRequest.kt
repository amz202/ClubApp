package com.example.clubapp.network.request

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EventRequest(
    val name: String,
    val description: String,
    val clubId: String?,
    val dateTime : String,
    val location: String,
    val capacity: String?,
    val organizedBy:String
)
