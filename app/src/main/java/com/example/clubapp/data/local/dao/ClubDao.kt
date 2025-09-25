package com.example.clubapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clubapp.data.local.entities.ClubEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClubDao {
    @Query("SELECT * FROM clubs ORDER BY lastUpdated DESC")
    fun getAllClubs(): Flow<List<ClubEntity>>  //using flow here to get real-time updates, suspend is for one-time fetch, like API call

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClubs(clubs: List<ClubEntity>)

    @Query("DELETE FROM clubs")
    suspend fun clearAllClubs()
}