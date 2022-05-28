package com.example.mymovie.repositories

import androidx.lifecycle.Transformations
import com.example.mymovie.database.*
import com.example.mymovie.models.*
//import com.example.mymovie.database.MovieUpcomingDao
import com.example.mymovie.request.MovieApiService
import com.example.mymovie.utils.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val movieApiService: MovieApiService,
    private val movieDao: MovieDao,
    private val movieUpcomingDao: MovieUpcomingDao,
    private val movieFavoriteMovieDao: FavoriteMovieDao
) {

    val moviesPopular = Transformations.map(movieDao.getPopularMovies()) {
        it.asDomainModel()
    }

    val movieUpcoming = Transformations.map(movieUpcomingDao.getUpcomingMovies()) {
        it.asDomainModelUpcoming()
    }

    val movieFavorite = Transformations.map(movieFavoriteMovieDao.getFavoriteMovies()) {
        it.asDomainModelFavorite()
    }

    suspend fun getMovieDetails(movieID: Int): MovieDetail {
        return movieApiService.getMoviesDetails(movieID, Credentials.API_KEY)
    }

    suspend fun getCastInfo(movieID: Int): CastingInfo {
        return movieApiService.getCastInfo(movieID, Credentials.API_KEY)
    }

    suspend fun searchMovieByName(queryName: String): MovieSearch {
        return movieApiService.searchMovies(Credentials.API_KEY, queryName)
    }

    // Insert favorite movie to local database
    suspend fun insertFavoriteMovie(
        movieID: Int,
        title: String,
        poster_path: String,
        genre1: String?,
        genre2: String?,
        vote_average: Double
    ) {
        withContext(Dispatchers.IO) {
            val databaseFavoriteMovie =
                DatabaseFavoriteMovie(movieID, title, poster_path, genre1, genre2, vote_average)
            movieFavoriteMovieDao.insertFavoriteMovie(databaseFavoriteMovie)
        }
    }

    suspend fun removeFavoriteMovie(movieFavorite: MovieFavorite) {
        withContext(Dispatchers.IO) {
            val movieFavoriteDatabase = DatabaseFavoriteMovie(
                movieFavorite.id,
                movieFavorite.title,
                movieFavorite.poster_path,
                movieFavorite.genre1,
                movieFavorite.genre2,
                movieFavorite.vote_average
            )
            movieFavoriteMovieDao.removeFavoriteMovie(movieFavoriteDatabase)
        }
    }

    suspend fun getMoviesUpcoming() {
        withContext(Dispatchers.IO) {
            val moviesUpcoming = movieApiService.getUpcomingMovie(Credentials.API_KEY, 1).movieList

            val movieDatabaseUpcoming = moviesUpcoming.map {
                DatabaseMovieUpcoming(
                    adult = it.adult,
                    backdrop_path = it.backdrop_path,
//                        genre_ids = it.genre_ids,
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
            movieUpcomingDao.insertToUpcomingMovieDB(movieDatabaseUpcoming)
        }
    }

    suspend fun getMoviesPopular() {
        withContext(Dispatchers.IO) {
            val moviesPopular = movieApiService.getDiscoveryMovies(Credentials.API_KEY, 1).movieList

            val movieDatabasePopular =
                moviesPopular.map {
                    DatabaseMovie(
                        adult = it.adult,
                        backdrop_path = it.backdrop_path,
//                        genre_ids = it.genre_ids,
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
            movieDao.insertToPopularMovieDB(movieDatabasePopular)
        }
    }

}