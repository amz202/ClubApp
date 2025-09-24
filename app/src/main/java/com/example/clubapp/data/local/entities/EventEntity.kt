package com.example.clubapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clubapp.network.response.EventResponse

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val clubId: String?,
    val clubName: String?,
    val dateTime: String,
    val location: String,
    val capacity: String?,
    val organizedBy: String,
    val attendeeCount: Int,
    val tags: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

fun EventEntity.toEventResponse() = EventResponse(
    id = id,
    name = name,
    description = description,
    clubId = clubId,
    clubName = clubName,
    dateTime = dateTime,
    location = location,
    capacity = capacity,
    organizedBy = organizedBy,
    attendeeCount = attendeeCount,
    tags = tags
)

fun EventResponse.toEventEntity() = EventEntity(
    id = id,
    name = name,
    description = description,
    clubId = clubId,
    clubName = clubName,
    dateTime = dateTime,
    location = location,
    capacity = capacity,
    organizedBy = organizedBy,
    attendeeCount = attendeeCount,
    tags = tags
)