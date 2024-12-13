package com.example.composemusicplayer.data.model


import com.google.gson.annotations.SerializedName

data class Data(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)

data class MusicModelItem(

    @field:SerializedName("song")
    val song: String? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("singer")
    val singer: List<String?>? = null,

    @field:SerializedName("album")
    val album: String? = null

)
