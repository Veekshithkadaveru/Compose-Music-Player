package com.example.composemusicplayer.di

import android.content.Context
import com.example.composemusicplayer.data.remote.ApiService
import com.example.composemusicplayer.data.repository.MusicRepository
import com.example.composemusicplayer.data.service.MusicControllerImpl
import com.example.composemusicplayer.domain.service.MusicController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService): MusicRepository {
        return MusicRepository(apiService)
    }

    @Singleton
    @Provides
    fun provideMusicController(@ApplicationContext context: Context): MusicController =
       MusicControllerImpl(context)

}