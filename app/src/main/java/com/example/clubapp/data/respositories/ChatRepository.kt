package com.example.clubapp.data.respositories

import com.example.clubapp.network.ApiService
import com.example.clubapp.network.request.EditMessageRequest
import com.example.clubapp.network.response.ChatMessageResponse
import okhttp3.ResponseBody

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
        return apiService.recentChat("Bearer $token", groupId) ?: emptyList()
    }
}