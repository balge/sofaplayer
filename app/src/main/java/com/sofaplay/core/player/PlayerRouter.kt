package com.sofaplay.core.player

import com.sofaplay.core.model.MediaSource
import com.sofaplay.core.model.PlaybackCapability

class PlayerRouter(
    private val engines: Map<String, PlayerEngineSpec>,
    private val preferredEngineId: String? = null,
) {
    fun resolve(source: MediaSource): String {
        val candidate = preferredEngineId
            ?: engines.keys.firstOrNull { engineSupports(it, source) }
            ?: engines.keys.first()
        require(engines.containsKey(candidate)) { "Unknown player engine: $candidate" }
        return candidate
    }

    private fun engineSupports(engineId: String, source: MediaSource): Boolean {
        val required = source.capabilities.ifEmpty { setOf(PlaybackCapability.DIRECT_FILE) }
        return engines.getValue(engineId).capabilities.supported.containsAll(required)
    }
}

data class PlayerEngineSpec(
    val id: String,
    val displayName: String,
    val capabilities: PlayerCapabilities,
    val factory: PlayerEngineFactory,
)
