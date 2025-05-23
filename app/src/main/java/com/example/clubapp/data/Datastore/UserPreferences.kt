package com.example.clubapp.data.Datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.clubapp.network.request.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    // Keys for storing data
    companion object {
        private val ROLE_KEY = stringPreferencesKey("user_role")
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val TOKEN_KEY = stringPreferencesKey("user_token")
        private val ID_KEY = stringPreferencesKey("id_key")
    }

    // Save user data
    suspend fun saveUser(user: AuthUser, token: String) {
        context.dataStore.edit { preferences ->
            preferences[ROLE_KEY] = user.role
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = token
            preferences[ID_KEY] = user.id.toString()
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getUserInfo(): UserInfo? {
        val preferences = context.dataStore.data.first()

        val name = preferences[NAME_KEY] ?: return null
        val email = preferences[EMAIL_KEY] ?: return null
        return UserInfo(name, email)
    }

    data class UserInfo(val name: String?, val email: String?)
}
