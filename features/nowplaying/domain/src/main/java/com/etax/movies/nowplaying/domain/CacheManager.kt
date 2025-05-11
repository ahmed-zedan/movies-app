package com.etax.movies.nowplaying.domain

interface CacheManager {
    suspend fun shouldInvalidateCache(): Boolean
    fun updateLastSync()
    fun setLastLoadedPage(page: Int)
    fun getLastLoadedPage(): Int?
}
