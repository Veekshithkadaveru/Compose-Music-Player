package com.example.composemusicplayer.data.repository

import com.example.composemusicplayer.data.model.MusicModelItem
import com.example.composemusicplayer.utils.network.DataState
import kotlinx.coroutines.flow.Flow

interface MusicRepositoryInterface {
    suspend fun loadMusic(): Flow<DataState<List<MusicModelItem>>>
}