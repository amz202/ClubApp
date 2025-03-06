package com.example.clubapp

import android.app.Application
import com.example.clubapp.data.AppContainer
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.data.DefaultAppContainer

class ClubApplication: Application() {
    lateinit var container: AppContainer
    lateinit var userPreferences: UserPreferences

    override fun onCreate() {
        super.onCreate()
        userPreferences = UserPreferences(this)
        container = DefaultAppContainer()
    }
}