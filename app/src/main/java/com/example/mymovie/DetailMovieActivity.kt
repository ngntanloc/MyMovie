package com.example.mymovie

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.mymovie.adapter.CastAdapter
import com.example.mymovie.databinding.ActivityDetailMovieBinding
import com.example.mymovie.models.Genre
import com.example.mymovie.models.MovieDetail
import com.example.mymovie.utils.Credentials
import com.example.mymovie.viewmodels.MovieApiStatus
import com.example.mymovie.viewmodels.MovieViewModel

class DetailMovieActivity : AppCompatActivity() {

    private val viewModel: MovieViewModel by viewModels()

    private val args: DetailMovieActivityArgs by navArgs()

    private lateinit var binding: ActivityDetailMovieBinding
    private var movieID: Int = 0

    private lateinit var movieDetail: MovieDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (intent.getIntExtra("movieID", 0) == 0) {
            movieID = args.movieId
        } else {
            movieID = intent.getIntExtra("movieID", 0)
        }

        val castAdapter = CastAdapter(
            onItemClicked = {
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.putExtra(SearchManager.QUERY, it.name)
                startActivity(intent)
            }
        )

        binding.imgBackdrop.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=${movieDetail.title} trailer"))
            startActivity(intent)
        }

        binding.txtStory.setOnClickListener {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, movieDetail.title)
            startActivity(intent)
        }

        viewModel.clickSpecificMovie(movieID)
        viewModel.getCastInfo(movieID)

        viewModel.movieDetailInformation.observe(this) {
            movieDetail = viewModel.movieDetailInformation.value!!

            binding.apply {
                txtMovieTitle.text = movieDetail.title
                txtImdbPoint.text = movieDetail.vote_average.toString()
                txtStory.text = movieDetail.overview
                bindGenre(it.genres)
                bindImage(it.backdrop_path)
            }
        }

        binding.btnClickedFavorite.setOnClickListener {
            Toast.makeText(this, "Added ${movieDetail.title} to favorite list", Toast.LENGTH_LONG).show()
            if (movieDetail.genres.size == 1) {
                viewModel.insertMovieFavorite(
                    movieID = movieDetail.id,
                    title = movieDetail.title,
                    poster_path = movieDetail.poster_path,
                    genre1 = movieDetail.genres[0].name,
                    genre2 = null,
                    vote_average = movieDetail.vote_average
                )
            }
            if (movieDetail.genres.size >= 2) {
                viewModel.insertMovieFavorite(
                    movieID = movieDetail.id,
                    title = movieDetail.title,
                    poster_path = movieDetail.poster_path,
                    genre1 = movieDetail.genres[0].name,
                    genre2 = movieDetail.genres[1].name,
                    vote_average = movieDetail.vote_average
                )
            }
        }

        binding.btnBack.setOnClickListener { super.onBackPressed() }

        viewModel.castInformation.observe(this) {
            val castInformation = viewModel.castInformation.value
            castAdapter.submitList(castInformation)
            binding.recyclerCast.adapter = castAdapter
            binding.recyclerCast.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun bindGenre(genres: List<Genre>) {
        val numberGenre = genres.size
        when {
            numberGenre == 1 -> {
                binding.txtGenre1.text = resources.getString(R.string.txt_genre1, genres[0].name)

                binding.txtGenre2.visibility = GONE
                binding.txtGenre3.visibility = GONE
            }
            numberGenre == 2 -> {
                binding.txtGenre1.text = resources.getString(R.string.txt_genre1, genres[0].name)
                binding.txtGenre2.text = resources.getString(R.string.txt_genre2, genres[1].name)

                binding.txtGenre3.visibility = GONE
            }
            numberGenre > 2 -> {
                binding.txtGenre1.text = resources.getString(R.string.txt_genre1, genres[0].name)
                binding.txtGenre2.text = resources.getString(R.string.txt_genre2, genres[1].name)
                binding.txtGenre3.text = resources.getString(R.string.txt_genre3, genres[2].name)
            }
        }
    }

    private fun bindImage(image_url: String) {
        val imgUrl = Credentials.GET_IMAGE + image_url
        imgUrl.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            binding.imgBackdrop.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
        }
    }

}