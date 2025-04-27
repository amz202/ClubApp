package com.example.clubapp.network.request

import kotlinx.serialization.Serializable

@Serializable
data class EventNewsRequest(
    val news:String,
    val eventId:String,
)