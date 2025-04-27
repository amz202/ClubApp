package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class EventNewsResponse(
    val id: String,
    val news: String,
    val createdOn: String,
    val eventId: String
)
