package com.etax.movies.nowplaying.domain

import androidx.paging.PagingData
import com.etax.movies.nowplaying.domain.entities.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPlayingMovies(queryTitle: String): Flow<PagingData<Movie>>
    suspend fun sync(): Result<Unit>
}

