package com.etax.movies.nowplaying.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieModel::class, MovieRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class PlayingMoviesDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao

    companion object {
        const val DATABASE_NAME = "now_playing_movies.db"
    }
}