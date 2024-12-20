package com.example.composemusicplayer.ui.view.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.composemusicplayer.data.repository.MusicRepository
import com.example.composemusicplayer.domain.usecase.AddMediaItemsUseCase
import com.example.composemusicplayer.domain.usecase.PauseSongUseCase
import com.example.composemusicplayer.domain.usecase.PlaySongUseCase
import com.example.composemusicplayer.domain.usecase.ResumeSongUseCase
import com.example.composemusicplayer.domain.usecase.SkipToNextSongUseCase
import com.example.composemusicplayer.domain.usecase.SkipToPreviousSongUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val musicRepository: MusicRepository,
    private val addMediaItemsUseCase: AddMediaItemsUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val skipToNextSongUseCase: SkipToNextSongUseCase,
    private val skipToPreviousSongUseCase: SkipToPreviousSongUseCase
) : ViewModel() {

    var homeUiState by mutableStateOf(HomeUiState())
        private set

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.FetchSong -> getSong()
            is HomeEvent.OnSongSelected -> homeUiState =
                homeUiState.copy(selectedSong = event.selectedSong)

            HomeEvent.PauseSong -> pauseSong()
            HomeEvent.PlaySong -> playSong()
            HomeEvent.ResumeSong -> resumeSong()
            HomeEvent.SkipToNextSong -> skipToNextSong()
            HomeEvent.SkipToPreviousSong -> skipToPreviousSong()
        }
    }

    private fun skipToPreviousSong() {
        TODO("Not yet implemented")
    }

    private fun skipToNextSong() {
        TODO("Not yet implemented")
    }

    private fun resumeSong() {
        TODO("Not yet implemented")
    }

    private fun playSong() {
        TODO("Not yet implemented")
    }

    private fun pauseSong() {
        TODO("Not yet implemented")
    }

    private fun getSong() {
        TODO("Not yet implemented")
    }
}