package com.etax.movies.nowplaying.persentation

import androidx.lifecycle.ViewModel
import com.etax.movies.nowplaying.domain.GetNowPlayingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) : ViewModel() {
    val movies = getNowPlayingMoviesUseCase("")
}
