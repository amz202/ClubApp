package com.example.clubapp.ui.viewModels

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
import com.example.clubapp.network.request.ClubEventsRequest
import com.example.clubapp.network.request.EventRequest
import com.example.clubapp.network.request.RoleRequest
import com.example.clubapp.network.response.EventParticipantsResponse
import com.example.clubapp.network.response.EventResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log

typealias EventUiState = BaseUiState<List<EventResponse>>
typealias EventParticipantUiState = BaseUiState<List<EventParticipantsResponse>>
typealias EventRoleUiState = BaseUiState<String?>

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

    private val eventCache = mutableMapOf<String, EventResponse>()
    private val participantsCache = mutableMapOf<String, List<EventParticipantsResponse>>()
    private val userEventsCache = mutableMapOf<String, List<EventResponse>>()
    private val clubEventsCache = mutableMapOf<String, List<EventResponse>>()

    init {
        getEvents()
    }

    fun getClubEvents(clubId: String) {
        viewModelScope.launch {
            if (clubEventsCache.containsKey(clubId)) {
                _clubEvents.value = clubEventsCache[clubId]
                return@launch
            }
            _clubEvents.value = emptyList()
            try {
                val events = eventRepository.getClubEvents(clubId)
                _clubEvents.value = events
                clubEventsCache[clubId] = events ?: emptyList()
            } catch (e: Exception) {
                _clubEvents.value = null
            }
        }
    }

    fun getEvents() {
        viewModelScope.launch {
            try {
                uiState = BaseUiState.Loading
                val events = eventRepository.getEvents()
                if (events.isEmpty()) {
                    uiState = BaseUiState.Error
                } else {
                    _events.value = events
                    uiState = BaseUiState.Success(events)
                }
            } catch (e: Exception) {
                uiState = BaseUiState.Error
                e.printStackTrace()
            }
        }
    }

    fun getMyEvents() {
        viewModelScope.launch {
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    return@launch
                }
                val cacheKey = token
                if (userEventsCache.containsKey(cacheKey)) {
                    _usersEvents.value = userEventsCache[cacheKey]
                    return@launch
                }
                val events = eventRepository.getMyEvents(token)
                if (!events.isNullOrEmpty()) {
                    _usersEvents.value = events
                    userEventsCache[cacheKey] = events
                }
            } catch (e: Exception) {
                _usersEvents.value = emptyList()
                e.printStackTrace()
            }
        }
    }

    fun getEvent(id: String) {
        viewModelScope.launch {
            if (eventCache.containsKey(id)) {
                _eventOfId.value = eventCache[id]
                return@launch
            }
            _eventOfId.value = null
            try {
                val event = eventRepository.getEvent(id)
                _eventOfId.value = event
                eventCache[id] = event
            } catch (e: Exception) {
                _eventOfId.value = null
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

    fun joinEvent(eventId: String) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.joinEvent(token, eventId)
                val updatedEvents = eventRepository.getEvents()
                uiState = BaseUiState.Success(updatedEvents)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun leaveEvent(eventId: String) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.leaveEvent(token, eventId)
                val updatedEvents = eventRepository.getEvents()
                uiState = BaseUiState.Success(updatedEvents)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun getEventParticipants(eventId: String) {
        viewModelScope.launch {
            if (participantsCache.containsKey(eventId)) {
                _eventParticipants.value = participantsCache[eventId] ?: emptyList()
                return@launch
            }
            _eventParticipants.value = emptyList()
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    _eventParticipants.value = emptyList()
                    return@launch
                }
                val participants = eventRepository.getEventParticipants(token, eventId)
                _eventParticipants.value = participants
                participantsCache[eventId]=participants
            } catch (e: Exception) {
                e.printStackTrace()
                _eventParticipants.value = emptyList()
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

    fun changeEventRole(eventId: String, request: RoleRequest) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                eventRepository.changeEventRole(token, eventId, request)
                val updatedEvents = eventRepository.getEvents()
                uiState = BaseUiState.Success(updatedEvents)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun getEventRole(eventId: String, userId: String) {
        viewModelScope.launch {
            eventRoleUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                val role = eventRepository.getEventRole(token, eventId, userId)
                eventRoleUiState = BaseUiState.Success(role)
            } catch (e: Exception) {
                eventRoleUiState = BaseUiState.Error
            }
        }
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