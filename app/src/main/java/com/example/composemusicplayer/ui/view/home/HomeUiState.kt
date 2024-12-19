package com.example.composemusicplayer.ui.view.home

import com.example.composemusicplayer.domain.model.Song

data class HomeUiState(
    val loading: Boolean? = false,
    val songs: List<Song>? = emptyList(),
    val selectedSong: Song? = null,
    val errorMessage: String? = null
)
