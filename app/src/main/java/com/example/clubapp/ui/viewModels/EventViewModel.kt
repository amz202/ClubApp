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
import com.example.clubapp.data.respositories.EventRepository
import com.example.clubapp.network.request.ClubEventsRequest
import com.example.clubapp.network.request.EventRequest
import kotlinx.coroutines.launch

typealias EventUiState = BaseUiState<List<EventRequest>>

class EventViewModel(private val eventRepository: EventRepository): ViewModel() {
    var uiState: EventUiState by mutableStateOf(BaseUiState.Loading)
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

    fun createEvent(token: String, event: EventRequest) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                eventRepository.createEvent(token, event)
                val updatedEvents = eventRepository.getEvents()
                uiState = BaseUiState.Success(updatedEvents)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }

    fun deleteEvent(token: String, id: String) {
        viewModelScope.launch {
            uiState = BaseUiState.Loading
            try {
                eventRepository.deleteEvent(token, id)
                val updatedEvents = eventRepository.getEvents()
                uiState = BaseUiState.Success(updatedEvents)
            } catch (e: Exception) {
                uiState = BaseUiState.Error
            }
        }
    }
    companion object {
        val eventFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClubApplication
                EventViewModel(app.container.eventRepository)
            }
        }
    }
}