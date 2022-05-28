package com.example.mymovie.models

data class MovieSearchItem(
    val adult: Boolean,
    val backdrop_path: String? = null,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String? = null,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)