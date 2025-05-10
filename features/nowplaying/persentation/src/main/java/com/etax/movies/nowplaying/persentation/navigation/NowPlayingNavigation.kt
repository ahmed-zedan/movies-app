package com.etax.movies.nowplaying.persentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.etax.movies.nowplaying.persentation.NowPlayingScreen
import kotlinx.serialization.Serializable

@Serializable
data object NowPlayingRoute

fun NavController.navigateToNowPlaying(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = NowPlayingRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.nowPlayingScreen(
    onMovieClick: (String) -> Unit,
) {
    composable<NowPlayingRoute> {
        NowPlayingScreen(
            onMovieClick = onMovieClick,
            viewModel = hiltViewModel()
        )
    }
}
