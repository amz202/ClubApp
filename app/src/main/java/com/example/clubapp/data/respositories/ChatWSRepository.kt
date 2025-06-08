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
                host = "192.168.208.28"
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