package com.example.clubapp.ui.navigation

import com.example.clubapp.network.response.ClubResponse
import com.example.clubapp.network.response.EventResponse
import kotlinx.serialization.Serializable

@Serializable
object SignInScreenNav

@Serializable
object HomeScreenNav

@Serializable
object ClubScreenNav

@Serializable
object EventScreenNav

@Serializable
data class AddEventNavA(
    val clubId: String? = null
)

@Serializable
data class AddEventNavB(
    val name: String,
    val description: String,
    val tags: String,
    val clubId: String? = null
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
    val eventName: String,
    val ownRole:String?
)

@Serializable
data class ClubDetailNav(
    val clubId: String
)

@Serializable
data class ClubMembersNav(
    val clubId: String,
    val clubName: String,
    val ownRole:String?
)