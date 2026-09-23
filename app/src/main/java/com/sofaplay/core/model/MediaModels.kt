package com.sofaplay.core.model

enum class MediaKind {
    MOVIE,
    SERIES,
    EPISODE,
    LIVE,
}

enum class IdentitySource {
    EMBY,
    TMDB,
    DOUBAN,
    IMDB,
    URL,
    FORWARD,
    STREMIO,
}

data class MediaIdentity(
    val source: IdentitySource,
    val value: String,
    val mediaKind: MediaKind? = null,
    val season: Int? = null,
    val episode: Int? = null,
) {
    val stableKey: String get() = "${source.name.lowercase()}:${value}"
}

data class MediaImage(
    val posterUrl: String? = null,
    val backdropUrl: String? = null,
    val detailPosterUrl: String? = null,
    val galleryUrls: List<String> = emptyList(),
)

data class MediaItem(
    val identity: MediaIdentity,
    val title: String,
    val originalTitle: String? = null,
    val overview: String? = null,
    val releaseDate: String? = null,
    val rating: Double? = null,
    val genres: List<String> = emptyList(),
    val durationMs: Long? = null,
    val images: MediaImage = MediaImage(),
    val episodes: List<MediaItem> = emptyList(),
    val related: List<MediaItem> = emptyList(),
) {
    init {
        require(episodes.size <= 1 || identity.mediaKind != MediaKind.MOVIE) {
            "A movie cannot contain multiple episodes"
        }
    }
}

data class MediaSource(
    val id: String,
    val displayName: String,
    val url: String,
    val providerId: String,
    val headers: Map<String, String> = emptyMap(),
    val isProbeable: Boolean = true,
    val capabilities: Set<PlaybackCapability> = emptySet(),
)

enum class PlaybackCapability {
    HLS,
    DASH,
    SMOOTH_STREAMING,
    RTMP,
    RTSP,
    HTTP_FLV,
    DIRECT_FILE,
    TORRENT,
    EXTERNAL_SUBTITLE,
    CHAPTERS,
}

data class SubtitleTrack(
    val id: String,
    val label: String,
    val language: String? = null,
    val url: String? = null,
    val isEmbedded: Boolean = false,
    val isForced: Boolean = false,
    val isDefault: Boolean = false,
)

data class DanmakuItem(
    val id: String,
    val text: String,
    val timeMs: Long,
    val lane: Int = 0,
    val colorArgb: Long = 0xFFFFFFFFL,
    val isTop: Boolean = false,
    val isBottom: Boolean = false,
)
