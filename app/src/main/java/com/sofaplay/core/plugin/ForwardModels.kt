package com.sofaplay.core.plugin

import com.sofaplay.core.model.DanmakuItem
import com.sofaplay.core.model.MediaItem
import com.sofaplay.core.model.MediaSource
import com.sofaplay.core.model.SubtitleTrack

data class ForwardWidgetMetadata(
    val id: String,
    val title: String,
    val description: String? = null,
    val author: String? = null,
    val site: String? = null,
    val version: String? = null,
    val requiredVersion: String? = null,
    val icon: String? = null,
    val detailCacheDuration: Long = 60,
    val globalParams: List<ForwardModuleParam> = emptyList(),
    val modules: List<ForwardModule>,
    val search: ForwardModule? = null,
)

data class ForwardModule(
    val id: String,
    val title: String,
    val type: ForwardModuleType? = null,
    val description: String? = null,
    val requiresWebView: Boolean = false,
    val functionName: String,
    val sectionMode: Boolean = false,
    val cacheDuration: Long = 3600,
    val params: List<ForwardModuleParam> = emptyList(),
)

enum class ForwardModuleType {
    VIDEO,
    DANMU,
    STREAM,
    SUBTITLE,
}

data class ForwardModuleParam(
    val name: String,
    val title: String,
    val type: ForwardParamType,
    val description: String? = null,
    val value: String? = null,
    val belongTo: ForwardParamCondition? = null,
    val placeholders: List<ForwardOption> = emptyList(),
    val enumOptions: List<ForwardOption> = emptyList(),
)

enum class ForwardParamType {
    INPUT,
    CONSTANT,
    ENUMERATION,
    COUNT,
    PAGE,
    OFFSET,
    LANGUAGE,
    USER_ID,
}

data class ForwardParamCondition(
    val paramName: String,
    val values: List<String>,
)

data class ForwardOption(
    val title: String,
    val value: String,
)

data class ForwardMediaItem(
    val id: String,
    val type: ForwardItemType,
    val title: String,
    val coverUrl: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val mediaType: String? = null,
    val rating: String? = null,
    val videoUrl: String? = null,
    val link: String? = null,
    val episodeItems: List<ForwardMediaItem> = emptyList(),
    val relatedItems: List<ForwardMediaItem> = emptyList(),
)

enum class ForwardItemType {
    URL,
    DETAIL,
    DOUBAN,
    IMDB,
    TMDB,
    FORWARD,
}

data class ForwardResource(
    val name: String,
    val description: String? = null,
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val playerType: String? = null,
)

data class ForwardSubtitle(
    val id: String,
    val title: String,
    val language: String,
    val count: Int,
    val url: String,
)

interface ForwardPluginHost {
    suspend fun listHomeSections(widgetId: String, moduleId: String, params: Map<String, String>): List<MediaItem>
    suspend fun search(widgetId: String, query: String): List<MediaItem>
    suspend fun detail(widgetId: String, link: String): MediaItem?
    suspend fun resources(widgetId: String, context: ForwardPlaybackContext): List<MediaSource>
    suspend fun subtitles(widgetId: String, context: ForwardPlaybackContext): List<SubtitleTrack>
    suspend fun danmaku(widgetId: String, context: ForwardPlaybackContext, segmentTimeMs: Long? = null): List<DanmakuItem>
}

data class ForwardPlaybackContext(
    val identityKey: String,
    val tmdbId: String? = null,
    val imdbId: String? = null,
    val mediaType: String,
    val title: String,
    val seriesName: String? = null,
    val episodeName: String? = null,
    val season: Int? = null,
    val episode: Int? = null,
    val link: String? = null,
    val videoUrl: String? = null,
)
