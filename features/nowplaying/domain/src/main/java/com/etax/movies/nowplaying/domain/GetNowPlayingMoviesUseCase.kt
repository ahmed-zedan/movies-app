package com.etax.movies.nowplaying.domain

import androidx.paging.PagingData
import com.etax.movies.nowplaying.domain.entities.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(queryTitle: String): Flow<PagingData<Movie>> {
        return movieRepository.getPlayingMovies(queryTitle)
    }
}
