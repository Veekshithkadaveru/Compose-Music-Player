package com.example.composemusicplayer.data.model

import com.example.composemusicplayer.domain.model.Song
import com.example.composemusicplayer.domain.other.PlayerState

data class MusicControllerUiState(
    val playerState: PlayerState? = null,
    val currentSong: Song? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOneEnabled: Boolean = false
)
