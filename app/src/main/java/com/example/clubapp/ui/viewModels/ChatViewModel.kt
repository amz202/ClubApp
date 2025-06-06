package com.example.clubapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.data.respositories.ChatRepository
import com.example.clubapp.data.respositories.ChatWSRepository
import com.example.clubapp.network.request.SentMessage
import com.example.clubapp.network.response.ChatMessageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository,
    private val socketRepository: ChatWSRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessageResponse>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessageResponse>> = _chatMessages

    fun initChat(groupId: String) {
        viewModelScope.launch {
            val token = userPreferences.getToken() ?: return@launch

            // Fetch recent messages via Retrofit
            val recentMessages = repository.recentChat(token, groupId)
            _chatMessages.value = recentMessages

            // Start WebSocket
            socketRepository.connect(groupId, token) { newMessage ->
                _chatMessages.update { it + newMessage }
            }
        }
    }

    fun sendMessage(text: String, groupId: String, sender: String) {
        val message = SentMessage(message = text, groupId = groupId, sender = sender)
        viewModelScope.launch {
            socketRepository.sendMessage(message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            socketRepository.disconnect()
        }
    }
}
