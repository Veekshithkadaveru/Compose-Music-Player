package com.example.composemusicplayer

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.composemusicplayer.data.service.MusicPlayerService
import com.example.composemusicplayer.navigation.NavHostScreen
import com.example.composemusicplayer.ui.view.activity.MainViewModel
import com.example.composemusicplayer.ui.viewmodel.NetworkViewModel
import com.example.composemusicplayer.ui.viewmodel.SharedViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModels by viewModels()
    private val splashViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen().apply {
            setOnExitAnimationListener { viewProvider ->
                ObjectAnimator.ofFloat(
                    viewProvider.view,
                    "scaleX",
                    0.5f, 0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 300
                    doOnEnd { viewProvider.remove() }
                    start()
                }
                ObjectAnimator.ofFloat(
                    viewProvider.view,
                    "scaleY",
                    0.5f, 0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 300
                    doOnEnd { viewProvider.remove() }
                    start()
                }
            }
        }
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }
        setContent {
            val networkViewModel = remember { NetworkViewModel(this) }
            val isConnected = networkViewModel.networkStatus

            val navController = rememberNavController()
            NavHostScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                isConnected = isConnected,
                modifier = Modifier
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.destroyMediaController()
        stopService(Intent(this, MusicPlayerService::class.java))
    }
}

@Composable
fun SplashCompose(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    )
}
