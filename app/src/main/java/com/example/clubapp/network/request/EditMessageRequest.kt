package com.example.clubapp.network.request

import kotlinx.serialization.Serializable

@Serializable
data class EditMessageRequest(
    val id: String,
    val newMessage: String
)