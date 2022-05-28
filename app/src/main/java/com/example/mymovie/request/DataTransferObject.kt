package com.example.mymovie.request

import com.example.mymovie.database.DatabaseMovie
import com.example.mymovie.database.MovieDatabase
import com.example.mymovie.models.MovieItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
//data class NetworkMovieContainer(
//    val page: Int,
//    @Json(name = "results")
//    val results: List<NetworkMovie>,
//    val total_pages: Int,
//    val total_results: Int
//)
//
//@JsonClass(generateAdapter = true)
//data class NetworkMovie(
//    val adult: Boolean,
//    val backdrop_path: String,
//    val genre_ids: List<Int>,
//    val id: Int,
//    val original_language: String,
//    val original_title: String,
//    val overview: String,
//    val popularity: Double,
//    val poster_path: String,
//    val release_date: String,
//    val title: String,
//    val video: Boolean,
//    val vote_average: Double,
//    val vote_count: Int
//)

//fun NetworkMovieContainer.asDomainModel(): List<MovieItem> {
//    return results.map {
//        MovieItem(
//            adult = it.adult,
//            backdrop_path = it.backdrop_path,
//            genre_ids = it.genre_ids,
//            id = it.id,
//            original_language = it.original_language,
//            original_title = it.original_title,
//            overview = it.overview,
//            popularity = it.popularity,
//            poster_path = it.poster_path,
//            release_date = it.release_date,
//            title = it.title,
//            video = it.video,
//            vote_average = it.vote_average,
//            vote_count = it.vote_count
//        )
//    }
//}


