package com.example.composemusicplayer.data.repository

import com.example.composemusicplayer.data.model.MusicModelItem
import com.example.composemusicplayer.data.remote.ApiService
import com.example.composemusicplayer.utils.network.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.await
import javax.inject.Inject

class MusicRepository @Inject constructor(private val apiService: ApiService) :
    MusicRepositoryInterface {
    override suspend fun loadMusic(): Flow<DataState<List<MusicModelItem>>> = flow {

        emit(DataState.Loading)
        try {
            val response = apiService.getMusic().await()
            emit(DataState.Success(response))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}