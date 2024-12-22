package com.example.composemusicplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemusicplayer.data.model.MusicControllerUiState
import com.example.composemusicplayer.domain.other.PlayerState
import com.example.composemusicplayer.domain.service.MusicController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SharedViewModels @Inject constructor(val musicController: MusicController) : ViewModel() {

    var musicControllerUiState by mutableStateOf(MusicControllerUiState())

    init {
        setMediaControllerCallBack()
    }

    private fun setMediaControllerCallBack() {
        musicController.mediaControllerCallback =
            { playerState, currentMusic, currentPosition, totalDuration, isShuffleEnabled, isRepeatOneEnabled ->
                musicControllerUiState = musicControllerUiState.copy(
                    playerState = playerState,
                    currentSong = currentMusic,
                    currentPosition = currentPosition,
                    totalDuration = totalDuration,
                    isShuffleEnabled = isShuffleEnabled,
                    isRepeatOneEnabled = isRepeatOneEnabled
                )

                if (playerState == PlayerState.PLAYING) {
                    viewModelScope.launch {
                        while (true) {
                            delay(1.seconds)
                            musicControllerUiState = musicControllerUiState.copy(
                                currentPosition = musicController.getCurrentPosition()
                            )
                        }
                    }
                }
            }
    }

    fun destroyMediaController() {
        musicController.destroy()
    }
}