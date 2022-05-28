package com.example.mymovie.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM databasemovie")
    fun getPopularMovies(): LiveData<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToPopularMovieDB(movies: List<DatabaseMovie>)
}

@Dao
interface MovieUpcomingDao {
    @Query("SELECT * FROM databasemovieupcoming")
    fun getUpcomingMovies(): LiveData<List<DatabaseMovieUpcoming>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToUpcomingMovieDB(movies: List<DatabaseMovieUpcoming>)

}

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM databasefavoritemovie")
    fun getFavoriteMovies(): LiveData<List<DatabaseFavoriteMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteMovie(movieFavorite: DatabaseFavoriteMovie)

    @Delete
    fun removeFavoriteMovie(movieFavorite: DatabaseFavoriteMovie)
}

@Database(entities = [DatabaseMovie::class, DatabaseMovieUpcoming::class, DatabaseFavoriteMovie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val movieUpcomingDao: MovieUpcomingDao
    abstract val favoriteMovieDao: FavoriteMovieDao
}

private lateinit var INSTANCE: MovieDatabase
fun getDatabase(context: Context): MovieDatabase {
    synchronized(MovieDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java,
                "movie_db"
            ).build()
        }
    }
    return INSTANCE
}