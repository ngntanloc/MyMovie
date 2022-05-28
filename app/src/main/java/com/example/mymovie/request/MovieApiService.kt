package com.example.mymovie.request

import com.example.mymovie.models.CastingInfo
import com.example.mymovie.models.Movie
import com.example.mymovie.models.MovieDetail
import com.example.mymovie.models.MovieSearch
import com.example.mymovie.utils.Credentials
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("/3/movie/popular")
    suspend fun getDiscoveryMovies(
        @Query("api_key") key: String?,
        @Query("page") page: Int
    ): Movie

    // Get upcoming movies
    // https://api.themoviedb.org/3/movie/upcoming?api_key=7910d3ba7d6a7c13cb6139493d9aa7ba&page=1
    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovie(
        @Query("api_key") key: String?,
        @Query("page") page: Int
    ): Movie

    // Search Specific Movie
    // https://api.themoviedb.org/3/movie/343611?api_key={api_key}
    @GET("/3/movie/{movie_id}")
    suspend fun getMoviesDetails(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String?
    ): MovieDetail

    @GET("/3/movie/{movie_id}/credits")
    suspend fun getCastInfo(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): CastingInfo

    @GET("/3/search/movie")
    suspend fun searchMovies(
        @Query("api_key") api_key: String,
        @Query("query") query: String
    ): MovieSearch

}

object MovieNetwork {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Credentials.BASE_URL)
        .build()

    val movieRetrofit = retrofit.create(MovieApiService::class.java)

}
