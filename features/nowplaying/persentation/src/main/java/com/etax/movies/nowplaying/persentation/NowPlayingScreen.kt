package com.etax.movies.nowplaying.persentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
internal fun NowPlayingScreen(
    onMovieClick: (String) -> Unit,
    viewModel: NowPlayingViewModel
) {
    Text("Now Playing")
}
