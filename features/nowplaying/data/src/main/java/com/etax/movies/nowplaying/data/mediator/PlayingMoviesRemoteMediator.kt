package com.etax.movies.nowplaying.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.etax.movies.nowplaying.data.local.MovieModel
import com.etax.movies.nowplaying.data.local.MovieRemoteKey
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
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: FIRST_PAGE
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
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
                    database.movieRemoteKeysDao().clearRemoteKeys()
                    database.movieDao().clear()
                    cacheManager.updateLastSync()
                }
                val prevKey = if (page == FIRST_PAGE) null else page - PAGE_OFFSET
                val nextKey = if (endOfPaginationReached) null else page + PAGE_OFFSET
                val keys = movies.map {
                    MovieRemoteKey(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.movieRemoteKeysDao().insertAll(keys)
                database.movieDao().insert(movies.map { it.toModel() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Throwable) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieModel>): MovieRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
                database.movieRemoteKeysDao().remoteKeysMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieModel>): MovieRemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                // Get the remote keys of the first items retrieved
                database.movieRemoteKeysDao().remoteKeysMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieModel>
    ): MovieRemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                database.movieRemoteKeysDao().remoteKeysMovieId(movieId)
            }
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val PAGE_OFFSET = 1
    }
}
