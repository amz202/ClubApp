package com.example.clubapp.data.respositories

import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.EditMessageRequest
import com.example.clubapp.network.response.ChatMessageResponse
import okhttp3.ResponseBody

interface ChatRepository {
    suspend fun editMessage(token: String, body: EditMessageRequest): ResponseBody
    suspend fun deleteMessage(token: String, id: String): ResponseBody
    suspend fun recentChat(token: String, groupId: String): List<ChatMessageResponse>?
}

class ChatRepositoryImpl(private val apiService: ApiService) : ChatRepository {
    override suspend fun editMessage(token: String, body: EditMessageRequest): ResponseBody {
        return apiService.editMessage(token = "Bearer $token", body = body)
    }

    override suspend fun deleteMessage(token: String, id: String): ResponseBody {
        return apiService.deleteMessage(token = "Bearer $token", id = id)
    }

    override suspend fun recentChat(token: String, groupId: String): List<ChatMessageResponse>? {
        return apiService.recentChat(token = "Bearer $token", groupId = groupId)
    }
}