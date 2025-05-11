package com.etax.movies.nowplaying.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.etax.movies.nowplaying.data.local.PlayingMoviesDatabase
import com.etax.movies.nowplaying.data.remote.MoviesRemoteService
import com.etax.movies.nowplaying.domain.CacheManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CacheManagerImpl @Inject constructor(
    private val api: MoviesRemoteService,
    private val db: PlayingMoviesDatabase,
    @ApplicationContext private val context: Context,
) : CacheManager {
    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            "now_playing_movies_cache",
            Context.MODE_PRIVATE
        )
    }

    override suspend fun shouldInvalidateCache(): Boolean {
        return isCacheExpired() || hasRemoteChanges()
    }

    private suspend fun hasRemoteChanges(): Boolean {
        return try {
            val localCount = db.movieDao().count()
            val remoteResponse = api.getMovieList(page = 1)
            val remoteTotal = remoteResponse.totalResults ?: return false

            preferences.getInt(LAST_TOTAL_RESULTS_KEY, -1).let { lastTotal ->
                if (lastTotal != remoteTotal || localCount != remoteTotal) {
                    preferences.edit { putInt(LAST_TOTAL_RESULTS_KEY, remoteTotal) }
                    true
                } else false
            }
        } catch (e: Throwable) {
            false
        }
    }

    private fun isCacheExpired(): Boolean {
        return System.currentTimeMillis() - preferences.getLong(LAST_SYNC_KEY, 0) > CACHE_TTL_MS
    }

    override fun updateLastSync() {
        preferences.edit {
            putLong(LAST_SYNC_KEY, System.currentTimeMillis())
        }
    }

    companion object {
        private const val LAST_SYNC_KEY = "last_sync"
        private const val LAST_TOTAL_RESULTS_KEY = "last_total_results"
        private const val CACHE_TTL_MS = 24 * 60 * 60 * 1000L // 24h
    }
}
