package com.example.composemusicplayer.utils

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerNotificationManager
import com.example.composemusicplayer.R
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

const val NOTIFICATION_LARGE_ICON_SIZE = 144
const val NOW_PLAYING_CHANNEL_ID = "media.NOW_PLAYING"
const val NOW_PLAYING_NOTIFICATION_ID = 0xb339

@OptIn(UnstableApi::class)
class MediaNotificationManager(
    context: Context,
    sessionToken: SessionToken,
    player: Player,
    notificationListener: PlayerNotificationManager.NotificationListener
) {
    private val serviceJob = SupervisorJob()
    val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaController.Builder(context, sessionToken).buildAsync()
        notificationManager = PlayerNotificationManager.Builder(
            context,
            NOW_PLAYING_NOTIFICATION_ID, NOW_PLAYING_CHANNEL_ID
        ).setChannelNameResourceId(R.string.media_notification_channel)
            .setChannelDescriptionResourceId(R.string.media_notification_channel_description)
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            .setNotificationListener(notificationListener)
            .setSmallIconResourceId(R.drawable.baseline_play_circle_outline_24)
            .build()
            .apply {
                setPlayer(player)
                setUseRewindAction(true)
                setUseFastForwardAction(true)
                setUseRewindActionInCompactView(true)
                setUseFastForwardActionInCompactView(true)
            }
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }

    inner class DescriptionAdapter(private val controller: ListenableFuture<MediaController>) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        var currentIconUri: Uri? = null
        val currentBitmap: Bitmap? = null
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return controller.get().mediaMetadata.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return controller.get().sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? = ""

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val iconUri = controller.get().mediaMetadata.artworkUri
            return if (currentIconUri != iconUri || currentBitmap == null) {
                currentIconUri = iconUri
                serviceScope.launch {
                    currentBitmap?.let { callback.onBitmap(it) }
                }
                null
            } else {
                currentBitmap
            }
        }

    }
}


