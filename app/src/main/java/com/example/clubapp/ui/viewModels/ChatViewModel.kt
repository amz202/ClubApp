package com.example.clubapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clubapp.ClubApplication
import com.example.clubapp.data.Datastore.UserPreferences
import com.example.clubapp.data.respositories.ChatRepository
import com.example.clubapp.data.respositories.ChatWSRepository
import com.example.clubapp.network.request.EditMessageRequest
import com.example.clubapp.network.request.SentMessage
import com.example.clubapp.network.response.ChatMessageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val socketRepository: ChatWSRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessageResponse>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessageResponse>> = _chatMessages

    fun initChat(groupId: String) {
        viewModelScope.launch {
            val token = userPreferences.getToken() ?: return@launch

            // Fetch recent messages via Retrofit
            val recentMessages = chatRepository.recentChat(token, groupId)
            if (recentMessages != null) {
                _chatMessages.value = recentMessages
            }

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

    fun editMessage(body: EditMessageRequest){
        viewModelScope.launch {
            val token = userPreferences.getToken() ?: return@launch
            chatRepository.editMessage(token, body)
        }
    }

    fun deleteMessage(id: String) {
        viewModelScope.launch {
            val token = userPreferences.getToken() ?: return@launch
            chatRepository.deleteMessage(token, id)
            _chatMessages.update { it.filterNot { message -> message.id == id } }
        }
    }

    companion object {
        val chatFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClubApplication
                ChatViewModel(
                    chatRepository = app.container.chatRepository,
                    socketRepository = app.container.chatWSRepository,
                    userPreferences = app.userPreferences
                )
            }
        }
    }
}
