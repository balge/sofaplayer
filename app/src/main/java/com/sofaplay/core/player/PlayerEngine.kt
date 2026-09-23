package com.sofaplay.core.player

import com.sofaplay.core.model.DanmakuItem
import com.sofaplay.core.model.MediaSource
import com.sofaplay.core.model.PlaybackCapability
import com.sofaplay.core.model.SubtitleTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

data class PlaybackProgress(
    val positionMs: Long,
    val bufferedMs: Long,
    val durationMs: Long,
    val speed: Float = 1.0f,
)

enum class PlaybackState {
    IDLE,
    PREPARING,
    READY,
    PLAYING,
    PAUSED,
    BUFFERING,
    ENDED,
    ERROR,
}

data class PlaybackRequest(
    val source: MediaSource,
    val subtitles: List<SubtitleTrack> = emptyList(),
    val startPositionMs: Long = 0,
    val autoplay: Boolean = true,
)

data class PlayerCapabilities(
    val supported: Set<PlaybackCapability>,
    val supportsSpeed: Boolean = true,
    val supportsFrameStep: Boolean = true,
    val supportsAudioSelection: Boolean = true,
    val supportsSubtitleSelection: Boolean = true,
)

interface PlayerEngine {
    val id: String
    val displayName: String
    val capabilities: PlayerCapabilities
    val state: StateFlow<PlaybackState>
    val progress: StateFlow<PlaybackProgress>
    val errors: Flow<Throwable>

    fun prepare(request: PlaybackRequest)
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun setSpeed(speed: Float)
    fun selectSubtitle(trackId: String?)
    fun selectAudio(trackId: String?)
    fun attachDanmaku(items: List<DanmakuItem>)
    fun release()
}

interface PlayerEngineFactory {
    fun create(): PlayerEngine
}
