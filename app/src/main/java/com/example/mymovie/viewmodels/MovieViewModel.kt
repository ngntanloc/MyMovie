package com.example.mymovie.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.mymovie.database.getDatabase
import com.example.mymovie.models.*
import com.example.mymovie.repositories.MovieRepository
import com.example.mymovie.request.MovieNetwork
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class MovieApiStatus {
    LOADING, ERROR, DONE
}

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private var movieRepository: MovieRepository = MovieRepository(
        MovieNetwork.movieRetrofit,
        getDatabase(application).movieDao,
        getDatabase(application).movieUpcomingDao,
        getDatabase(application).favoriteMovieDao
    )

    private var _statusPopularMovie = MutableLiveData<MovieApiStatus>()
    val statusPopular: LiveData<MovieApiStatus> = _statusPopularMovie

    //    private val _moviesPopular = MutableLiveData<List<MovieItem>>()
    val moviesPopular: LiveData<List<MovieItem>> = movieRepository.moviesPopular

    //    private val _moviesUpcoming = MutableLiveData<List<MovieItem>>()
    val moviesUpcoming: LiveData<List<MovieItem>> = movieRepository.movieUpcoming

    private var _statusUpcomingMovie = MutableLiveData<MovieApiStatus>()
    val statusUpcomingMovie: LiveData<MovieApiStatus> = _statusUpcomingMovie

    private val _movieDetailInformation = MutableLiveData<MovieDetail>()
    val movieDetailInformation: LiveData<MovieDetail> = _movieDetailInformation

    private var _movieDetailStatus = MutableLiveData<MovieApiStatus>(MovieApiStatus.LOADING)
    val movieDetailStatus: LiveData<MovieApiStatus> = _movieDetailStatus

    private val _castInformation = MutableLiveData<List<Cast>>()
    val castInformation: LiveData<List<Cast>> = _castInformation

    private val _movieSearching = MutableLiveData<List<MovieSearchItem>>()
    val movieSearch: LiveData<List<MovieSearchItem>> = _movieSearching

    val favoriteMovie: LiveData<List<MovieFavorite>> = movieRepository.movieFavorite

    init {
        refreshDataFromNetwork()
    }

    fun insertMovieFavorite(
        movieID: Int,
        title: String,
        poster_path: String,
        genre1: String?,
        genre2: String?,
        vote_average: Double
    ) = viewModelScope.launch {
        try {
            movieRepository.insertFavoriteMovie(
                movieID,
                title,
                poster_path,
                genre1,
                genre2,
                vote_average
            )
        } catch (networkError: IOException) {

        }
    }

    fun clickSpecificMovie(movieID: Int) = viewModelScope.launch {
        _movieDetailStatus.value = MovieApiStatus.LOADING
        try {
            _movieDetailInformation.value = movieRepository.getMovieDetails(movieID)
            _movieDetailStatus.value = MovieApiStatus.DONE
        } catch (networkError: IOException) {
            _movieDetailStatus.value = MovieApiStatus.ERROR
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun searchMovieByName(queryName: String) = viewModelScope.launch {
        try {

//            val movieSource = movieRepository.searchMovieByName(queryName).movieSearching
//            val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//
//            val sortedListByDate = movieSource.sortedByDescending {
//                LocalDate.parse(it.release_date, dateTimeFormatter)
//            }

            _movieSearching.value = movieRepository.searchMovieByName(queryName).movieSearching

        } catch (networkError: IOException) {
            _movieSearching.value = emptyList()
        }
    }

    fun removeFavoriteMovie(movieFavorite: MovieFavorite) = viewModelScope.launch {
        try {
            val movieDatabaseFavorite = movieRepository.removeFavoriteMovie(movieFavorite)
        } catch (networkError: IOException) {

        }
    }

    fun getCastInfo(movieID: Int) = viewModelScope.launch {
        try {
            _castInformation.value = movieRepository.getCastInfo(movieID).cast
        } catch (networkError: IOException) {

        }
    }

    private fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            movieRepository.getMoviesPopular()
            _statusPopularMovie.value = MovieApiStatus.DONE

            movieRepository.getMoviesUpcoming()
            _statusUpcomingMovie.value = MovieApiStatus.DONE

        } catch (networkError: IOException) {

            _statusPopularMovie.value = MovieApiStatus.ERROR
            _statusUpcomingMovie.value = MovieApiStatus.ERROR
        }
    }

}
