package com.example.mymovie.models

import com.squareup.moshi.Json

data class Movie(
    val page: Int,
    @Json(name = "results")
    var movieList: List<MovieItem>,
    val total_pages: Int,
    val total_results: Int
)
