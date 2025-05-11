package com.etax.movies.nowplaying.data.local

import androidx.paging.PagingSource
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.etax.movies.nowplaying.domain.entities.Movie

@Entity(tableName = "movies")
data class MovieModel(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "adult") val adult: Boolean?,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "original_language") val originalLanguage: String?,
    @ColumnInfo(name = "original_title") val originalTitle: String?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "popularity") val popularity: Float?,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "video") val video: Boolean?,
    @ColumnInfo(name = "vote_average") val voteAverage: Float?,
    @ColumnInfo(name = "vote_count") val voteCount: Int?,
)


@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies: List<MovieModel>)

    @Query("SELECT * FROM movies WHERE " +
            "title LIKE :queryTitle OR original_title LIKE :queryTitle " +
             "ORDER BY release_date DESC"
    )
    fun getAll(queryTitle: String): PagingSource<Int, MovieModel>

    @Query("SELECT id FROM movies")
    suspend fun getIds(): List<Long>

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun count(): Int

    @Query("DELETE FROM movies")
    suspend fun clear()
}


fun MovieModel.toEntity() = Movie(
    adult = adult,
    backdropPath = backdropPath,
    id = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = "https://image.tmdb.org/t/p/w500$posterPath",
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)