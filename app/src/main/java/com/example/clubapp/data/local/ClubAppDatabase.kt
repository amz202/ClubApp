package com.example.clubapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clubapp.data.local.entities.ClubEntity
import com.example.clubapp.data.local.entities.EventEntity
import com.example.clubapp.data.local.dao.ClubDao
import com.example.clubapp.data.local.dao.EventDao

@Database(
    entities = [ClubEntity::class, EventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ClubAppDatabase : RoomDatabase() {
    abstract fun clubDao(): ClubDao
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: ClubAppDatabase? = null

        fun getDatabase(context: Context): ClubAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClubAppDatabase::class.java,
                    "clubapp_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}