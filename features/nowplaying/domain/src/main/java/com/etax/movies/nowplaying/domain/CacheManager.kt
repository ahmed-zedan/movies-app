package com.etax.movies.nowplaying.domain

interface CacheManager {
    suspend fun shouldInvalidateCache(): Boolean
    fun updateLastSync()
}
