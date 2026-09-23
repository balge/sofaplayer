package com.sofaplay.core.provider

import com.sofaplay.core.model.MediaItem
import kotlin.math.min

object SourceMatcher {
    fun score(candidate: MediaItem, target: MediaItem): Int {
        if (candidate.identity == target.identity) return 100

        var score = 0
        if (candidate.originalTitle.equals(target.originalTitle, ignoreCase = true)) score += 45
        if (candidate.title.equals(target.title, ignoreCase = true)) score += 35
        if (candidate.releaseDate == target.releaseDate) score += 12
        if (candidate.identity.mediaKind == target.identity.mediaKind) score += 5
        score += titleSimilarity(candidate.title, target.title)
        return score
    }

    fun titleSimilarity(left: String, right: String): Int {
        val normalizedLeft = left.trim().lowercase()
        val normalizedRight = right.trim().lowercase()
        if (normalizedLeft.isEmpty() || normalizedRight.isEmpty()) return 0
        if (normalizedLeft == normalizedRight) return 20

        val commonLength = normalizedLeft.commonPrefixWith(normalizedRight).length
        return (20 * commonLength) / min(normalizedLeft.length, normalizedRight.length)
    }

    fun bestMatch(candidates: List<MediaItem>, target: MediaItem, threshold: Int = 70): MediaItem? {
        return candidates
            .map { it to score(it, target) }
            .filter { it.second >= threshold }
            .maxByOrNull { it.second }
            ?.first
    }
}
