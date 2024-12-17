package com.example.composemusicplayer.domain.usecase

import com.example.composemusicplayer.domain.model.Song
import com.example.composemusicplayer.domain.other.PlayerState
import com.example.composemusicplayer.domain.service.MusicController
import javax.inject.Inject

class SetMediaControllerCallbackUseCase @Inject constructor(
    private val musicController: MusicController
) {
    operator fun invoke(
        callback: (
            playerState: PlayerState,
            currentSong: Song?,
            currentPosition: Long,
            totalDuration: Long,
            isShuffleEnabled: Boolean,
            isRepeatOneEnabled: Boolean
        ) -> Unit
    ) {
        musicController.mediaControllerCallback = callback
    }
}