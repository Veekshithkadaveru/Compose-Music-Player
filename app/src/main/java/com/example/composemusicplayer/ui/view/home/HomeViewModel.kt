package com.example.composemusicplayer.ui.view.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemusicplayer.data.repository.MusicRepository
import com.example.composemusicplayer.domain.model.Song
import com.example.composemusicplayer.domain.usecase.AddMediaItemsUseCase
import com.example.composemusicplayer.domain.usecase.PauseSongUseCase
import com.example.composemusicplayer.domain.usecase.PlaySongUseCase
import com.example.composemusicplayer.domain.usecase.ResumeSongUseCase
import com.example.composemusicplayer.domain.usecase.SkipToNextSongUseCase
import com.example.composemusicplayer.domain.usecase.SkipToPreviousSongUseCase
import com.example.composemusicplayer.utils.network.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
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

    private fun skipToPreviousSong() = skipToPreviousSongUseCase {
        homeUiState = homeUiState.copy(selectedSong = it)
    }

    private fun skipToNextSong() = skipToNextSongUseCase {
        homeUiState = homeUiState.copy(selectedSong = it)
    }

    private fun resumeSong() = resumeSongUseCase

    private fun playSong() {
        homeUiState.apply {
            songs?.indexOf(selectedSong)?.let {
                playSongUseCase(it)
            }
        }
    }

    private fun pauseSong() = pauseSongUseCase

    private fun getSong() {
        homeUiState = homeUiState.copy(loading = true)

        viewModelScope.launch {
            musicRepository.loadMusic()
                .catch {
                    homeUiState = homeUiState.copy(
                        loading = false,
                        errorMessage = it.message
                    )
                }.collect {
                    homeUiState = when (it) {
                        is DataState.Error -> homeUiState.copy(
                            loading = false,
                            errorMessage = it.exception.message
                        )

                        DataState.Loading -> homeUiState.copy(
                            loading = true, errorMessage = null
                        )

                        is DataState.Success -> {
                            val mediaitemList = it.data?.let { song ->
                                song.map {
                                    Song(
                                        imageUrl = it.data?.image!!,
                                        songUrl = it.data?.url!!,
                                        title = it.song!!,
                                        subtitle = it.album!!,
                                        mediaId = it.song!!
                                    )
                                }
                            }
                            addMediaItemsUseCase(mediaitemList!!)
                            homeUiState.copy(
                                loading = false,
                                songs = mediaitemList
                            )
                        }

                    }
                }
        }
    }
}