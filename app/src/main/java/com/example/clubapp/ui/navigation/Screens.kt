package com.example.clubapp.ui.navigation

import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.network.response.EventResponse
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenNav

@Serializable
object ClubScreenNav

@Serializable
object EventScreenNav

@Serializable
object AddEventNavA

@Serializable
data class AddEventNavB(
    val name: String,
    val description: String,
    val tags: String
)

@Serializable
object AddClubNav

@Serializable
data class EventDetailNav(
    val eventId: String,
)

@Serializable
data class EventParticipantsNav(
    val eventId: String,
    val eventName: String
)
