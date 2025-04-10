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
import com.example.clubapp.network.response.EventParticipantResponse
import kotlinx.coroutines.launch

typealias EventUiState = BaseUiState<List<EventRequest>>
typealias EventParticipantUiState = BaseUiState<List<EventParticipantResponse>>
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

    fun getClubEvents(clubEventsRequest: ClubEventsRequest) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val events = eventRepository.getClubEvents(clubEventsRequest)
                uiState = BaseUiState.Success(events)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun getEvents() {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val events = eventRepository.getEvents()
                uiState = BaseUiState.Success(events)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun getEvent(id: String) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                val event = eventRepository.getEvent(id)
                uiState = BaseUiState.Success(listOf(event))
            } catch (e: Exception) {
                uiState = BaseUiState.Error
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
            eventParticipantUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    uiState = BaseUiState.Error
                    return@launch
                }
                val participants = eventRepository.getEventParticipants(token, eventId)
                eventParticipantUiState = BaseUiState.Success(participants)
            } catch (e: Exception) {
                eventParticipantUiState = BaseUiState.Error
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