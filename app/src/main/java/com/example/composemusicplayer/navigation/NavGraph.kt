package com.example.composemusicplayer.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composemusicplayer.ui.view.VideoPlayerScreen
import com.example.composemusicplayer.ui.view.home.Home
import com.example.composemusicplayer.ui.view.home.HomeEvent
import com.example.composemusicplayer.ui.view.home.HomeViewModel
import com.example.composemusicplayer.ui.viewmodel.SharedViewModels
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NavHostScreen(
    modifier: Modifier,
    navController: NavHostController,
    sharedViewModel: SharedViewModels,
    isConnected: StateFlow<Boolean>
) {

    val musicControllerUiState = sharedViewModel.musicControllerUiState
    val activity = (LocalContext.current as ComponentActivity)

    NavHost(navController = navController, startDestination = "/", modifier = modifier) {

        composable(route = "/") {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            val isInitialized = rememberSaveable { mutableStateOf(false) }

            if (!isInitialized.value) {
                LaunchedEffect(key1 = Unit) {
                    homeViewModel.onEvent(HomeEvent.FetchSong)
                    isInitialized.value = true
                }
            }
            Home(
                navController = navController,
                onEvent = homeViewModel::onEvent,
                uiState = homeViewModel.homeUiState,
                playerState = musicControllerUiState.playerState,
                isConnected = isConnected,
                musicControllerUiState = musicControllerUiState
            )
        }
        composable(route = "video") {
            VideoPlayerScreen(
                modifier = modifier,
                navController = navController,
                isConnected = isConnected
            )
        }
    }
}