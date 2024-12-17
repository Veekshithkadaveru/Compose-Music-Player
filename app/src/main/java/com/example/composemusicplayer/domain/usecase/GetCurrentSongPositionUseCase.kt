package com.example.composemusicplayer.domain.usecase

import com.example.composemusicplayer.domain.service.MusicController
import javax.inject.Inject

class GetCurrentSongPositionUseCase @Inject constructor(private val musicController: MusicController) {

    operator fun invoke() = musicController.getCurrentPosition()
}