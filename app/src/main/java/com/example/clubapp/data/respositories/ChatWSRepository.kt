package com.example.clubapp.data.respositories

import com.example.clubapp.network.request.SentMessage
import com.example.clubapp.network.response.ChatMessageResponse
import com.google.android.gms.auth.api.Auth
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.Job
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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

interface ChatWSRepository {
    suspend fun connect(groupId: String, token: String, clubId: String, onMessage: (ChatMessageResponse) -> Unit)
    suspend fun sendMessage(message: SentMessage)
    suspend fun disconnect()
}

class ChatWSRepositoryImpl : ChatWSRepository {
    private var session: WebSocketSession? = null
    private var receiveJob: Job? = null

    override suspend fun connect(
        groupId: String,
        token: String,
        clubId:String,
        onMessage: (ChatMessageResponse) -> Unit
    ) {
        // Ensure the token is properly formatted as "Bearer token"
        val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"

        val client = HttpClient(CIO) {
            install(WebSockets)
            install(Auth) {
                bearer {
                    loadTokens {
                        // Extract just the token part without "Bearer "
                        BearerTokens(formattedToken.replace("Bearer ", ""), "")
                    }
                }
            }
        }
        session = client.webSocketSession {
            url {
                protocol = URLProtocol.WS
                host = "192.168.0.159"
                port = 8001
                encodedPath = "/chat/${clubId}/$groupId"
            }
        }
        receiveJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                for (frame in session?.incoming ?: return@launch) {
                    if (frame is Frame.Text) {
                        val json = frame.readText()
                        val message = Json.decodeFromString<ChatMessageResponse>(json)
                        onMessage(message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun sendMessage(message: SentMessage) {
        val json = Json.encodeToString(message)
        session?.send(Frame.Text(json))
    }

    override suspend fun disconnect() {
        receiveJob?.cancelAndJoin()
        session?.close()
        session = null
    }
}