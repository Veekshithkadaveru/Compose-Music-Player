package com.example.composemusicplayer.data.mapper

import androidx.media3.common.MediaItem
import com.example.composemusicplayer.domain.model.Song

fun MediaItem.toSong() =
    Song(
        mediaId = mediaId ?: "Unknown Media ID",
        title = mediaMetadata.title?.toString() ?: "Unknown Title",
        subtitle = mediaMetadata.subtitle?.toString() ?: "Unknown Subtitle",
        songUrl = mediaId ?: "Unknown URL",
        imageUrl = mediaMetadata.artworkUri?.toString() ?: ""
    )

