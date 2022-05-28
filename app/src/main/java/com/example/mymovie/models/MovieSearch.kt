package com.example.mymovie.models

import com.squareup.moshi.Json

data class MovieSearch(
    val page: Int,
    @Json(name = "results")
    val movieSearching: List<MovieSearchItem>,
    val total_pages: Int,
    val total_results: Int
)