package com.example.mymovie.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymovie.models.MovieFavorite
import com.example.mymovie.models.MovieItem

@Entity
data class DatabaseMovie constructor(
    val adult: Boolean,
    val backdrop_path: String,
//    val genre_ids: List<Int>,
    @PrimaryKey
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

@Entity
data class DatabaseMovieUpcoming constructor(
    val adult: Boolean,
    val backdrop_path: String,
//    val genre_ids: List<Int>,
    @PrimaryKey
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

@Entity
data class DatabaseFavoriteMovie constructor(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster_path: String,
    val genre1: String? = null,
    val genre2: String? = null,
    val vote_average: Double
)

fun List<DatabaseFavoriteMovie>.asDomainModelFavorite(): List<MovieFavorite> {
    return map {
        MovieFavorite(
            id = it.id,
            title = it.title,
            poster_path = it.poster_path,
            genre1 = it.genre1,
            genre2 = it.genre2,
            vote_average = it.vote_average
        )
    }
}

fun List<DatabaseMovie>.asDomainModel(): List<MovieItem> {
    return map {
        MovieItem(
            adult = it.adult,
            backdrop_path = it.backdrop_path,
//            genre_ids = it.genre_ids,
            id = it.id,
            original_language = it.original_language,
            original_title = it.original_title,
            overview = it.overview,
            popularity = it.popularity,
            poster_path = it.poster_path,
            release_date = it.release_date,
            title = it.title,
            video = it.video,
            vote_average = it.vote_average,
            vote_count = it.vote_count
        )
    }
}

fun List<DatabaseMovieUpcoming>.asDomainModelUpcoming(): List<MovieItem> {
    return map {
        MovieItem(
            adult = it.adult,
            backdrop_path = it.backdrop_path,
//            genre_ids = it.genre_ids,
            id = it.id,
            original_language = it.original_language,
            original_title = it.original_title,
            overview = it.overview,
            popularity = it.popularity,
            poster_path = it.poster_path,
            release_date = it.release_date,
            title = it.title,
            video = it.video,
            vote_average = it.vote_average,
            vote_count = it.vote_count
        )
    }
}

