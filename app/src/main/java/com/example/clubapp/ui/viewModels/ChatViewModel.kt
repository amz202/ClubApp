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
import com.example.clubapp.data.respositories.ChatRepository
import com.example.clubapp.data.respositories.ChatWSRepository
import com.example.clubapp.network.request.EditMessageRequest
import com.example.clubapp.network.request.SentMessage
import com.example.clubapp.network.response.ChatMessageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

typealias ChatUiState = BaseUiState<List<ChatMessageResponse>>
class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val socketRepository: ChatWSRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var chatUiState: ChatUiState by mutableStateOf(BaseUiState.Loading)
        private set

    private val _chatMessages = MutableStateFlow<List<ChatMessageResponse>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessageResponse>> = _chatMessages


    fun initChat(groupId: String, clubId: String) {
        viewModelScope.launch {
            chatUiState = BaseUiState.Loading
            try {
                val token = userPreferences.getToken()
                if (token == null) {
                    chatUiState = BaseUiState.Error
                    return@launch
                }

                try {
                    val recentMessages = chatRepository.recentChat(token, groupId)
                    if (recentMessages != null) {
                        _chatMessages.value = recentMessages
                        chatUiState = BaseUiState.Success(recentMessages)
                    } else {
                        _chatMessages.value = emptyList()
                        chatUiState = BaseUiState.Success(emptyList())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _chatMessages.value = emptyList()
                    chatUiState = BaseUiState.Success(emptyList())
                }

                // Start WebSocket (continue even if message fetch failed)
                socketRepository.connect(groupId, token, clubId) { newMessage ->
                    _chatMessages.update { it + newMessage }
                    chatUiState = BaseUiState.Success(_chatMessages.value)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                chatUiState = BaseUiState.Error
            }
        }
    }

    fun sendMessage(text: String, groupId: String, sender: String, senderName: String) {
        val message = SentMessage(message = text, groupId = groupId, sender = sender, senderName =senderName )
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

    fun closeSocket(){
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
