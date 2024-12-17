package com.example.composemusicplayer.data.service

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.composemusicplayer.data.mapper.toSong
import com.example.composemusicplayer.domain.model.Song
import com.example.composemusicplayer.domain.other.PlayerState
import com.example.composemusicplayer.domain.service.MusicController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class MusicControllerImpl(context: Context) : MusicController {

    private var mediaControllerFuture: ListenableFuture<MediaController>

    private val mediaController: MediaController?
        get() = if (mediaControllerFuture.isDone) mediaControllerFuture.get() else null

    override var mediaControllerCallback:
            ((
                playerState: PlayerState,
                currentMusic: Song?,
                currentPosition: Long,
                totalDuration: Long,
                isShuffleEnabled: Boolean,
                isRepeatOneEnabled: Boolean
            ) -> Unit)? = null

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicPlayerService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture.addListener({ controllerListener() }, MoreExecutors.directExecutor())

    }

    private fun controllerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                with(player) {
                    mediaControllerCallback?.invoke(
                        playbackState.toPlayerState(isPlaying),
                        currentMediaItem?.toSong(),
                        currentPosition.coerceAtLeast(0L),
                        duration.coerceAtLeast(0L),
                        shuffleModeEnabled,
                        repeatMode == Player.REPEAT_MODE_ONE
                    )
                }
            }

            override fun onPlayerErrorChanged(error: PlaybackException?) {
                Log.e("ExoPlayerChange", "Error: -----==== ${error?.message}")
            }

            override fun onPlayerError(error: PlaybackException) {
                Log.e("ExoPlayer", "Error: ====-----==== ${error.message}")
            }
        })
    }

    private fun Int.toPlayerState(isPlaying: Boolean): PlayerState {
        return when (this) {
            Player.STATE_IDLE -> PlayerState.STOPPED
            Player.STATE_ENDED -> PlayerState.STOPPED
            else -> if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
        }
    }

    override fun addMediaItems(songs: List<Song>) {
        TODO("Not yet implemented")
    }

    override fun play(mediaItemIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun getCurrentPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    override fun skipToNextSong() {
        TODO("Not yet implemented")
    }

    override fun skipToPreviousSong() {
        TODO("Not yet implemented")
    }

    override fun getCurrentSong(): Song {
        TODO("Not yet implemented")
    }

    override fun seekTo(position: Long) {
        TODO("Not yet implemented")
    }
}