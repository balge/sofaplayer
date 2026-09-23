package com.sofaplay.core.provider

import com.sofaplay.core.model.MediaItem
import com.sofaplay.core.model.MediaSource

interface MediaProvider {
    val id: String
    val displayName: String

    suspend fun homeSections(): List<MediaSection>
    suspend fun search(query: String): List<MediaItem>
    suspend fun detail(identityKey: String): MediaItem?
    suspend fun sources(identityKey: String): List<MediaSource>
}

data class MediaSection(
    val id: String,
    val title: String,
    val templateId: String = "posterRow",
    val items: List<MediaItem>,
)
