package com.etax.movieapp.di

import android.content.Context
import androidx.room.Room
import com.etax.movies.nowplaying.data.CacheManagerImpl
import com.etax.movies.nowplaying.data.MovieRepositoryImpl
import com.etax.movies.nowplaying.data.local.PlayingMoviesDatabase
import com.etax.movies.nowplaying.data.remote.MoviesRemoteService
import com.etax.movies.nowplaying.domain.CacheManager
import com.etax.movies.nowplaying.domain.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NowPlayingFeatureModuleProvider {

    @Provides
    @Singleton
    fun provideMoviesRemoteService(retrofit: Retrofit): MoviesRemoteService {
        return retrofit.create(MoviesRemoteService::class.java)
    }

    @Provides
    @Singleton
    fun providePlayingMoviesDatabase(@ApplicationContext context: Context): PlayingMoviesDatabase {
        return Room.databaseBuilder(
            context,
            PlayingMoviesDatabase::class.java,
            PlayingMoviesDatabase.DATABASE_NAME
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface NowPlayingFeatureModuleBinder {

    @Binds
    @Singleton
    fun bindCacheManager(impl: CacheManagerImpl): CacheManager

    @Binds
    @Singleton
    fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository
}
