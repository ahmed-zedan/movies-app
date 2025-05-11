package com.etax.movies.nowplaying.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.etax.movies.nowplaying.data.local.PlayingMoviesDatabase
import com.etax.movies.nowplaying.data.local.toEntity
import com.etax.movies.nowplaying.data.mediator.PlayingMoviesRemoteMediator
import com.etax.movies.nowplaying.data.remote.MoviesRemoteService
import com.etax.movies.nowplaying.data.remote.toModel
import com.etax.movies.nowplaying.domain.CacheManager
import com.etax.movies.nowplaying.domain.MovieRepository
import com.etax.movies.nowplaying.domain.entities.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val service: MoviesRemoteService,
    private val database: PlayingMoviesDatabase,
    private val cacheManager: CacheManager
) : MovieRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPlayingMovies(queryTitle: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(30, enablePlaceholders = false),
            remoteMediator = PlayingMoviesRemoteMediator(service, database, cacheManager)
        ) {
            database.movieDao().getAll("%${queryTitle.replace(' ', '%')}%")
        }.flow
            .map { data -> data.map { movie -> movie.toEntity() } }
    }

    override suspend fun sync(): Result<Unit> = runCatching {
        var currentPage = 1 // cacheManager.getLastLoadedPage()
        var totalPages = Int.MAX_VALUE

        while (currentPage <= totalPages) {
            val response = service.getMovieList(currentPage)

            database.withTransaction {
                if (currentPage == 1) {
                    database.movieDao().clear()
                }
                database.movieDao().insert(response.movies?.map { it.toModel() } ?: emptyList())
            }

            cacheManager.setLastLoadedPage(currentPage)
            currentPage++
            totalPages = response.totalPages ?: 0
        }

        cacheManager.updateLastSync()
    }
}
