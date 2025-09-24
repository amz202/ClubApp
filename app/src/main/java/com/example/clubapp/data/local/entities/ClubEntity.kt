package com.example.clubapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clubapp.network.response.ClubResponse

@Entity(tableName = "clubs")
data class ClubEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val createdBy: String,
    val tags: String,
    val createdOn: String,
    val memberCount: Int,
    val status: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

fun ClubEntity.toClubResponse() = ClubResponse(
    id = id,
    name = name,
    description = description,
    createdBy = createdBy,
    tags = tags,
    createdOn = createdOn,
    memberCount = memberCount,
    status = status
)

fun ClubResponse.toClubEntity() = ClubEntity(
    id = id,
    name = name,
    description = description,
    createdBy = createdBy,
    tags = tags,
    createdOn = createdOn,
    memberCount = memberCount,
    status = status
)