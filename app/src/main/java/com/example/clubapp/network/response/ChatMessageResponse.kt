package com.example.clubapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    val id: String,
    val sender: String,
    val message: String,
    val timeStamp: String,
    val groupId: String
)