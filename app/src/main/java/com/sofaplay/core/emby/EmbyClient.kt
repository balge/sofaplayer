package com.sofaplay.core.emby

import com.sofaplay.core.model.MediaItem
import kotlinx.coroutines.flow.Flow

data class EmbyCredentials(
    val baseUrl: String,
    val username: String,
    val password: String,
)

data class EmbySession(
    val userId: String,
    val accessToken: String,
    val serverId: String? = null,
)

interface EmbyClient {
    val connectionState: Flow<EmbyConnectionState>

    suspend fun login(credentials: EmbyCredentials): EmbySession
    suspend fun libraries(): List<MediaItem>
    suspend fun items(parentId: String, page: Int, pageSize: Int = 60): List<MediaItem>
    suspend fun item(itemId: String): MediaItem
    suspend fun playableUrl(itemId: String): String
    suspend fun reportProgress(itemId: String, positionMs: Long, isPaused: Boolean)
    suspend fun reportStopped(itemId: String, positionMs: Long)
}

enum class EmbyConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    AUTHENTICATION_FAILED,
    NETWORK_ERROR,
}
