package com.example.composemusicplayer.ui.view.home

import android.util.Log
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.composemusicplayer.R
import com.example.composemusicplayer.data.model.MusicControllerUiState
import com.example.composemusicplayer.domain.model.Song
import com.example.composemusicplayer.domain.other.PlayerState
import com.example.composemusicplayer.ui.components.NetworkOff
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onEvent: (HomeEvent) -> Unit,
    uiState: HomeUiState,
    playerState: PlayerState?,
    isConnected: StateFlow<Boolean>,
    musicControllerUiState: MusicControllerUiState
) {

    val coroutineScope = rememberCoroutineScope()
    val sheetSheet = rememberStandardBottomSheetState(
        skipHiddenState = true,
        initialValue = SheetValue.PartiallyExpanded
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetSheet)

    val isCon = isConnected.collectAsState().value
    Log.e("IsConnected", "Home: $isCon")

    if (isCon) {
        BottomSheetScaffold(
            modifier = Modifier,
            sheetContent = {
                Box(
                    modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures { change, dragAmount ->
                                Log.e("TAG", "Home: ------>>>> $dragAmount")
                                if (dragAmount > 0) {
                                    coroutineScope.launch {
                                        sheetSheet.partialExpand()
                                    }
                                } else {
                                    coroutineScope.launch {
                                        sheetSheet.expand()
                                    }
                                }
                            }
                        }
                ) {
                    with(uiState) {
                        when {
                            loading == true -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LinearProgressIndicator()
                                }
                            }

                            loading == false && errorMessage == null -> {
                                PlayerView(
                                    onEvent,
                                    playerState,
                                    uiState,
                                    musicControllerUiState,
                                    sheetSheet.currentValue
                                )
                            }

                            errorMessage != null -> {
                                Box(modifier = Modifier.background(Color.Black)) {

                                }
                            }
                        }
                    }
                }
            },
            sheetShape = RoundedCornerShape(20.dp),
            sheetPeekHeight = 200.dp,
            scaffoldState = bottomSheetScaffoldState
        ) {
            Box(modifier = Modifier.padding(top = 20.dp)) {
                with(uiState) {
                    when {
                        loading == true -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        loading == false -> {
                            if (songs != null) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.padding(it)
                                ) {
                                    items(count = songs.size) {
                                        MusicItem(song = songs[it]) {
                                            onEvent(HomeEvent.OnSongSelected(songs[it]))
                                            onEvent(HomeEvent.PlaySong)
                                        }
                                    }
                                }
                            }
                        }

                        errorMessage != null -> {
                            Box(
                                modifier = Modifier
                                    .background(color = Color.Black)
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {}
                        }
                    }
                }
            }
        }
    } else {
        NetworkOff()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MusicItem(song: Song, onClick: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Card(modifier = Modifier.padding(10.dp), onClick = { onClick() }) {
            GlideImage(
                model = song.imageUrl,
                contentDescription = "",
                loading = placeholder(R.drawable.ic_launcher_background),
                modifier = Modifier.size(150.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = song.title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(0.7f),
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun PlayerView(
    onEvent: (HomeEvent) -> Unit,
    playerState: PlayerState?,
    uiState: HomeUiState,
    musicControllerUiState: MusicControllerUiState,
    currentValue: SheetValue
) {

    val animatedAlign by animateIntOffsetAsState(
        targetValue = if (currentValue == SheetValue.PartiallyExpanded) {
            IntOffset.Zero
        } else {
            IntOffset(0, 1600)
        },
        label = "offset"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        GlideImage(
            model = musicControllerUiState.currentSong?.imageUrl,
            contentDescription = "",
            loading = placeholder(R.drawable.ic_launcher_background),
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(
                color = Color.Black.copy(0.9f),
                blendMode = BlendMode.SrcOver
            ),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = musicControllerUiState.currentPosition.toFloat() / musicControllerUiState.totalDuration.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlButton(icon = R.drawable.baseline_skip_previous_24, size = 50.dp, onClick = {
                    onEvent.invoke(HomeEvent.SkipToPreviousSong)
                })
                Spacer(modifier = Modifier.width(20.dp))
                Log.e("Screen State", "PlayerView: $$$$$ ${playerState?.name}")

                ControlButton(
                    icon = if (playerState == PlayerState.PLAYING)
                        R.drawable.baseline_pause_circle_outline_24
                    else
                        R.drawable.baseline_play_circle_outline_24,
                    size = 100.dp,
                    onClick = {
                        if (playerState == PlayerState.PLAYING) {
                            onEvent(HomeEvent.PauseSong)
                        } else {
                            onEvent(HomeEvent.ResumeSong)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                ControlButton(icon = R.drawable.baseline_skip_next_24, size = 50.dp, onClick = {
                    onEvent.invoke(HomeEvent.SkipToNextSong)
                })
            }
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "${musicControllerUiState.currentSong?.title}",
                Modifier.basicMarquee()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        musicControllerUiState.currentSong?.imageUrl.let {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                VinylAnimation(
                    imageUrl = it,
                    isPlaySong = musicControllerUiState.playerState == PlayerState.PLAYING
                )
            }
        }
    }
}


@Composable
fun ControlButton(icon: Int, size: Dp, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(size / 1.5f),
            painter = painterResource(id = icon),
            contentDescription = null
        )
    }
}
