package com.etax.movies.nowplaying.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesRemoteService {

    @GET("movie/now_playing")
    suspend fun getMovieList(@Query("page") page: Int): MovieListResponse
}