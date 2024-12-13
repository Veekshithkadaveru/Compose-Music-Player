package com.example.composemusicplayer.data.remote

import com.example.composemusicplayer.data.model.MusicModelItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("JSON/music.json")
    fun getMusic():Call<List<MusicModelItem>>
}