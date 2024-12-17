package com.example.composemusicplayer.domain.usecase

import com.example.composemusicplayer.domain.model.Song
import com.example.composemusicplayer.domain.service.MusicController
import javax.inject.Inject

class SkipToPreviousSongUseCase @Inject constructor(private val musicController: MusicController) {

    operator fun invoke(updateHomeUi: (Song?) -> Unit) {
        musicController.skipToPreviousSong()
        updateHomeUi(musicController.getCurrentSong())
    }
}