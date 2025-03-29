package com.example.clubapp.data.Datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.clubapp.network.request.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension to create DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    // Keys for storing data
    companion object {
        private val ROLE_KEY = stringPreferencesKey("user_role")
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val TOKEN_KEY = stringPreferencesKey("user_token")
    }

    // Save user data
    suspend fun saveUser(user: AuthUser, token: String) {
        context.dataStore.edit { preferences ->
            preferences[ROLE_KEY] = user.role
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = token
        }
    }

    // Get user role as a Flow
    val userRole: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[ROLE_KEY] }

    // Get full user data as a Flow
    val userData: Flow<AuthUser?> = context.dataStore.data
        .map { preferences ->
            val role = preferences[ROLE_KEY]
            val name = preferences[NAME_KEY]
            val email = preferences[EMAIL_KEY]
            if (role != null && name != null && email != null) {
                AuthUser(id = "", name = name, email = email, role = role) // Assuming ID isn't stored
            } else null
        }

    // Clear user data (for logout)
    suspend fun clearUser() {
        context.dataStore.edit { it.clear() }
    }
}
