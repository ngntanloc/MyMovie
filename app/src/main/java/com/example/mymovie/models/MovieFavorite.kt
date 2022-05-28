package com.example.mymovie.models

data class MovieFavorite(
    val id: Int,
    val title: String,
    val poster_path: String,
    val genre1: String?=null,
    val genre2: String?=null,
    val vote_average: Double
)
