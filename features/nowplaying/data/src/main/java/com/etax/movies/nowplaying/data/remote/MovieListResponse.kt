package com.etax.movies.nowplaying.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep
import com.etax.movies.nowplaying.data.local.MovieModel

@Keep
@JsonClass(generateAdapter = true)
data class MovieListResponse(
    @Json(name = "results") val movies: List<MovieDTO>?,
    @Json(name = "dates") val dates: MovieListDatesDTO?,
    @Json(name = "page") val page: Int?,
    @Json(name = "total_pages") val totalPages: Int?,
    @Json(name = "total_results") val totalResults: Int?,
)


@Keep
@JsonClass(generateAdapter = true)
data class MovieListDatesDTO(
    @Json(name = "maximum") val maximum: String,
    @Json(name = "minimum") val minimum: String,
)

@Keep
@JsonClass(generateAdapter = true)
data class MovieDTO(
    @Json(name = "adult") val adult: Boolean?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>?,
    @Json(name = "id") val id: Long,
    @Json(name = "original_language") val originalLanguage: String?,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "overview") val overview: String?,
    @Json(name = "popularity") val popularity: Float?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "video") val video: Boolean?,
    @Json(name = "vote_average") val voteAverage: Float?,
    @Json(name = "vote_count") val voteCount: Int?,
)

fun MovieDTO.toModel() = MovieModel(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)