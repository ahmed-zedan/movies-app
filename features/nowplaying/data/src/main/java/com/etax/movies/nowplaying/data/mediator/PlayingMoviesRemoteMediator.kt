package com.etax.movies.nowplaying.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.etax.movies.nowplaying.data.local.MovieModel
import com.etax.movies.nowplaying.data.local.PlayingMoviesDatabase
import com.etax.movies.nowplaying.data.remote.MoviesRemoteService
import com.etax.movies.nowplaying.data.remote.toModel
import com.etax.movies.nowplaying.domain.CacheManager

@OptIn(ExperimentalPagingApi::class)
class PlayingMoviesRemoteMediator(
    private val service: MoviesRemoteService,
    private val database: PlayingMoviesDatabase,
    private val cacheManager: CacheManager
) : RemoteMediator<Int, MovieModel>() {

    override suspend fun initialize(): InitializeAction {
        if (cacheManager.shouldInvalidateCache()) {
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }

        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieModel>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                FIRST_PAGE
            }

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            LoadType.APPEND -> {
                val lastPage = cacheManager.getLastLoadedPage()

                if (lastPage == -2)
                    return MediatorResult.Success(endOfPaginationReached = true)

                if (lastPage == null)
                    return MediatorResult.Success(endOfPaginationReached = false)

                lastPage + PAGE_OFFSET
            }
        }

        try {
            val response = service.getMovieList(page)
            val movies = response.movies ?: emptyList()
            val endOfPaginationReached =
                movies.isEmpty() || (response.totalPages != null && page >= response.totalPages)

            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.movieDao().clear()
                    cacheManager.updateLastSync()
                }


                cacheManager.setLastLoadedPage(page)
                database.movieDao().insert(movies.map { it.toModel() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Throwable) {
            return MediatorResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val PAGE_OFFSET = 1
    }
}
