package com.example.clubapp.network.request

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EventRequest(
    //Screen 1
    val name: String,
    val description: String,
    val tags:String,
    //Screen 2
    val location: String,
    val dateTime : String,
    val capacity: String?,

    val organizedBy:String,
    val clubId: String?,
)
