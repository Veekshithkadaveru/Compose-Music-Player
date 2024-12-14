package com.example.composemusicplayer.data.mapper

import androidx.media3.common.MediaItem
import com.example.composemusicplayer.domain.model.Song

fun MediaItem.toSong() =
    Song(
        mediaId = mediaId,
        title = mediaMetadata.title.toString(),
        subtitle = mediaMetadata.subtitle.toString(),
        songUrl = mediaId,
        imageUrl = mediaMetadata.artworkUri.toString()
    )
