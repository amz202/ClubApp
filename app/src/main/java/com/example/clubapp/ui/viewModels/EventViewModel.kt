package com.example.clubapp.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clubapp.ClubApplication
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.data.respositories.EventRepository
import com.example.clubapp.network.request.EventNewsRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.EventNewsResponse
import com.example.clubapp.network.response.EventParticipantsResponse
import com.example.clubapp.network.response.EventResponse
import com.example.clubapp.network.response.RoleResponse
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.containsKey
import kotlin.collections.remove
import kotlin.text.get

/*
 * Copyright 2025 Abdul Majid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

typealias EventUiState = BaseUiState<List<EventResponse>>
typealias EventParticipantUiState = BaseUiState<List<EventParticipantsResponse>>
typealias EventRoleUiState = BaseUiState<String?>
typealias SingleEventUiState = BaseUiState<EventResponse>
typealias UserEventsUiState = BaseUiState<List<EventResponse>>
typealias ClubEventsUiState = BaseUiState<List<EventResponse>>
typealias EventActionUiState = BaseUiState<Boolean>
typealias EventNewsUiState = BaseUiState<List<EventNewsResponse>>

class EventViewModel(
    private val eventRepository: EventRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    var uiState: EventUiState by mutableStateOf(BaseUiState.Loading)
        private set
    var eventParticipantUiState: EventParticipantUiState by mutableStateOf(BaseUiState.Loading)
        private set
    var eventRoleUiState: EventRoleUiState by mutableStateOf(BaseUiState.Loading)
        private set
    var singleEventUiState: SingleEventUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var userEventsUiState: UserEventsUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var clubEventsUiState: ClubEventsUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var joinEventUiState: EventActionUiState by mutableStateOf(BaseUiState.Success(false))
        private set

    var leaveEventUiState: EventActionUiState by mutableStateOf(BaseUiState.Loading)
        private set

    var eventNewsUiState: EventNewsUiState by mutableStateOf(BaseUiState.Loading)
        private set

    private val _usersEvents = MutableStateFlow<List<EventResponse>?>(emptyList())
    val usersEvents: StateFlow<List<EventResponse>?> =_usersEvents

    private val _events = MutableStateFlow<List<EventResponse>>(emptyList())
    val events: StateFlow<List<EventResponse>> = _events

    private val _eventOfId = MutableStateFlow<EventResponse?>(null)
    val eventOfId: StateFlow<EventResponse?> = _eventOfId

    private val _eventParticipants = MutableStateFlow<List<EventParticipantsResponse>>(emptyList())
    val eventParticipants: StateFlow<List<EventParticipantsResponse>> = _eventParticipants

    private val _clubEvents = MutableStateFlow<List<EventResponse>?>(emptyList())
    val clubEvents: StateFlow<List<EventResponse>?> = _clubEvents

    private val _userEventRole = MutableStateFlow<RoleResponse?>(null)
    val userEventRole: StateFlow<RoleResponse?> = _userEventRole

    private val _eventNews = MutableStateFlow<List<EventNewsResponse>?>(emptyList())
    val eventNews: StateFlow<List<EventNewsResponse>?> = _eventNews

    private val eventCache = mutableMapOf<String, EventResponse>()
    private val participantsCache = mutableMapOf<String, List<EventParticipantsResponse>>()
    private val userEventsCache = mutableMapOf<String, List<EventResponse>>()
    private val clubEventsCache = mutableMapOf<String, List<EventResponse>>()
    private val userEventRoleCache = mutableMapOf<String, RoleResponse?>()
    private val eventNewsCache = mutableMapOf<String, List<EventNewsResponse>>()

    init {
        getEvents()
    }

    fun getEvents() {
        viewModelScope.launch {
            try {
                uiState = BaseUiState.Loading
                val events = eventRepository.getEvents()
//                if (events.isEmpty()) {
//                    uiState = BaseUiState.Error
//                } else {
//                    _events.value = events
//                    uiState = BaseUiState.Success(events)
//                }
                _events.value = events
                uiState = BaseUiState.Success(events)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun createEvent(event: EventRequest) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.createEvent(token, event)
                val updatedEvents = eventRepository.getEvents()
                uiState = BaseUiState.Success(updatedEvents)
                val newEvent = updatedEvents.firstOrNull { it.name == event.name }
                if (newEvent != null) {
                    // Subscribe to event notifications
                    FirebaseMessaging.getInstance().subscribeToTopic("event_${newEvent.id}")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("FCM", "Subscribed to event_${newEvent.id}")
                            } else {
                                Log.e("FCM", "Failed to subscribe to event_${newEvent.id}")
                            }
                        }
                }
                val cacheKey = token
                val events = eventRepository.getMyEvents(token)
                if (!events.isNullOrEmpty()) {
                    _usersEvents.value = events
                    userEventsCache[cacheKey] = events
                    userEventsUiState = BaseUiState.Success(events)
                } else {
                    userEventsUiState = BaseUiState.Success(emptyList())
                }
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.deleteEvent(token, id)
                val updatedEvents = eventRepository.getEvents()
                uiState = BaseUiState.Success(updatedEvents)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }
    fun getUserEvents(userId: String) {
        viewModelScope.launch {
            eventParticipantUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                val events = eventRepository.getUserEvents(token, userId)
                eventParticipantUiState = BaseUiState.Success(events)
            } catch (e: Exception) {
                eventParticipantUiState = BaseUiState.Error
            }
        }
    }

    fun changeEventParticipantRole(eventId: String, request: RoleRequest, userId: String) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.changeEventRole(token, eventId, request, userId)
                participantsCache.remove(eventId)
                getEventParticipants(eventId)
                val updatedEvents = eventRepository.getEvents()
                uiState = BaseUiState.Success(updatedEvents)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun getClubEvents(clubId: String) {
        viewModelScope.launch {
            clubEventsUiState = BaseUiState.Loading
            if (clubEventsCache.containsKey(clubId)) {
                _clubEvents.value = clubEventsCache[clubId]
                clubEventsUiState = BaseUiState.Success(clubEventsCache[clubId] ?: emptyList())
                return@launch
            }
            _clubEvents.value = emptyList()
            try {
                val events = eventRepository.getClubEvents(clubId)
                _clubEvents.value = events
                clubEventsCache[clubId] = events ?: emptyList()
                clubEventsUiState = BaseUiState.Success(events ?: emptyList())
            } catch (e: Exception) {
                _clubEvents.value = null
                clubEventsUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getMyEvents() {
        viewModelScope.launch {
            userEventsUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    userEventsUiState = BaseUiState.Error
                    return@launch
                }
                val cacheKey = token
                if (userEventsCache.containsKey(cacheKey)) {
                    _usersEvents.value = userEventsCache[cacheKey]
                    userEventsUiState = BaseUiState.Success(userEventsCache[cacheKey] ?: emptyList())
                    return@launch
                }
                val events = eventRepository.getMyEvents(token)
                if (!events.isNullOrEmpty()) {
                    _usersEvents.value = events
                    userEventsCache[cacheKey] = events
                    userEventsUiState = BaseUiState.Success(events)
                } else {
                    userEventsUiState = BaseUiState.Success(emptyList())
                }
            } catch (e: Exception) {
                _usersEvents.value = emptyList()
                userEventsUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getEvent(id: String) {
        viewModelScope.launch {
            singleEventUiState = BaseUiState.Loading
            if (eventCache.containsKey(id)) {
                _eventOfId.value = eventCache[id]
                singleEventUiState = BaseUiState.Success(eventCache[id]!!)
                return@launch
            }
            _eventOfId.value = null
            try {
                val event = eventRepository.getEvent(id)
                _eventOfId.value = event
                eventCache[id] = event
                singleEventUiState = BaseUiState.Success(event)
            } catch (e: Exception) {
                _eventOfId.value = null
                singleEventUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun joinEvent(eventId: String) {
        viewModelScope.launch {
            joinEventUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    joinEventUiState = BaseUiState.Error
                    uiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.joinEvent(token, eventId)

                val updatedEvents = eventRepository.getEvents()
                _events.value = updatedEvents

                val updatedEvent = updatedEvents.find { it.id == eventId }
                if (updatedEvent != null && eventCache.containsKey(eventId)) {
                    eventCache[eventId] = updatedEvent
                    _eventOfId.value = updatedEvent
                }
                userEventsCache.remove(token)
                participantsCache.remove(eventId)
                userEventRoleCache.remove(eventId)
                val cacheKey = token
                val events = eventRepository.getMyEvents(token)
                if (!events.isNullOrEmpty()) {
                    _usersEvents.value = events
                    userEventsCache[cacheKey] = events
                    userEventsUiState = BaseUiState.Success(events)
                } else {
                    userEventsUiState = BaseUiState.Success(emptyList())
                }
                joinEventUiState = BaseUiState.Success(true)

                FirebaseMessaging.getInstance().subscribeToTopic("event_${eventId}")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FCM", "Subscribed to event_$eventId")
                        } else {
                            Log.e("FCM", "Failed to subscribe to event_$eventId")
                        }
                    }
            } catch (e: Exception) {
                joinEventUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun leaveEvent(eventId: String) {
        viewModelScope.launch {
            leaveEventUiState = BaseUiState.Loading
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    leaveEventUiState = BaseUiState.Error
                    uiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.leaveEvent(token, eventId)
                val updatedEvents = eventRepository.getEvents()
                leaveEventUiState = BaseUiState.Success(true)

                // Clear cache entries
                val cacheKey = token
                userEventsCache.remove(cacheKey)
                participantsCache.remove(eventId)
                userEventRoleCache.remove(eventId)

                _events.value = updatedEvents
                uiState = BaseUiState.Success(updatedEvents)
                FirebaseMessaging.getInstance().unsubscribeFromTopic("event_${eventId}")
            } catch (e: Exception) {
                leaveEventUiState = BaseUiState.Error
                uiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getEventParticipants(eventId: String) {
        viewModelScope.launch {
            eventParticipantUiState = BaseUiState.Loading
            if (participantsCache.containsKey(eventId)) {
                _eventParticipants.value = participantsCache[eventId] ?: emptyList()
                eventParticipantUiState = BaseUiState.Success(participantsCache[eventId] ?: emptyList())
                return@launch
            }
            _eventParticipants.value = emptyList()
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    _eventParticipants.value = emptyList()
                    eventParticipantUiState = BaseUiState.Error
                    return@launch
                }
                val participants = eventRepository.getEventParticipants(token, eventId)
                _eventParticipants.value = participants
                participantsCache[eventId] = participants
                eventParticipantUiState = BaseUiState.Success(participants)
            } catch (e: Exception) {
                e.printStackTrace()
                _eventParticipants.value = emptyList()
                eventParticipantUiState = BaseUiState.Error
            }
        }
    }

    fun getEventRole(eventId: String) {
        viewModelScope.launch {
            eventRoleUiState = BaseUiState.Loading
            if(userEventRoleCache.containsKey(eventId)) {
                _userEventRole.value = userEventRoleCache[eventId]
                eventRoleUiState = BaseUiState.Success(userEventRoleCache[eventId]?.role)
                return@launch
            }
            _userEventRole.value = null
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    eventRoleUiState = BaseUiState.Error
                    return@launch
                }
                val role = eventRepository.getEventRole(token, eventId)
                _userEventRole.value = role
                userEventRoleCache[eventId] = role
                eventRoleUiState = BaseUiState.Success(role?.role)
            } catch (e: Exception) {
                _userEventRole.value = null
                eventRoleUiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getEventNews(eventId: String) {
        viewModelScope.launch {
            eventNewsUiState = BaseUiState.Loading
            if (eventNewsCache.containsKey(eventId)) {
                _eventNews.value = eventNewsCache[eventId] ?: emptyList()
                eventNewsUiState = BaseUiState.Success(eventNewsCache[eventId] ?: emptyList())
                return@launch
            }
            _eventNews.value = emptyList()
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    eventNewsUiState = BaseUiState.Error
                    return@launch
                }
                val news = eventRepository.getEventNews(token, eventId)
                _eventNews.value = news
                eventNewsCache[eventId] = news ?: emptyList()
                eventNewsUiState = BaseUiState.Success(news ?: emptyList())
            } catch (e: Exception) {
                _eventNews.value = emptyList()
                eventNewsUiState = BaseUiState.Error
            }
        }
    }

    fun createEventNews(eventId: String, news: EventNewsRequest){
        viewModelScope.launch {
            eventNewsUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    eventNewsUiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.createEventNews(token, eventId, news)
                val updatedNews = eventRepository.getEventNews(token, eventId)
                if (updatedNews != null) {
                    _eventNews.value = updatedNews
                }
                eventNewsCache[eventId] = updatedNews ?: emptyList()
                eventNewsUiState = BaseUiState.Success(updatedNews ?: emptyList())
            } catch (e: Exception) {
                _eventNews.value = emptyList()
                eventNewsUiState = BaseUiState.Error
            }
        }
    }

    fun clearJoinUiState() {
        joinEventUiState = BaseUiState.Success(true)
    }

    fun clearEventState(){
        eventCache.clear()
        userEventRoleCache.clear()
    }

    companion object {
        val eventFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClubApplication
                EventViewModel(app.container.eventRepository, app.userPreferences)
            }
        }
    }
}
//suspend fun joinEvent(token: String, eventId: String): ResponseBody
//suspend fun leaveEvent(token: String, eventId: String): ResponseBody
//suspend fun getEventParticipants(token: String, eventId: String): List<EventParticipantResponse>
//suspend fun getUserEvents(token: String, userId: String): List<EventResponse>
//suspend fun changeEventRole(token: String, eventId: String, request: RoleRequest): ResponseBody