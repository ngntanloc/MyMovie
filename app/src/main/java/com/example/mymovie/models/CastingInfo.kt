package com.example.mymovie.models

import com.squareup.moshi.Json

data class CastingInfo(
    val id: Int,
    @Json(name = "cast")
    val cast: List<Cast>
)