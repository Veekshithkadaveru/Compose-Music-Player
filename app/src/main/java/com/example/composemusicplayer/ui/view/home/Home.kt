package com.example.composemusicplayer.ui.view.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerView(
    onEvent: (HomeEvent) -> Unit,
    playerState: PlayerState?,
    uiState: HomeUiState,
    musicControllerUiState: MusicControllerUiState,
    currentValue: SheetValue
) {

}
