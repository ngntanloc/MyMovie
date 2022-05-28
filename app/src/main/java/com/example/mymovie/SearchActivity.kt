package com.example.mymovie

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovie.adapter.SearchAdapter
import com.example.mymovie.databinding.ActivitySearchBinding
import com.example.mymovie.fragment.DiscoverFragmentDirections
import com.example.mymovie.viewmodels.MovieViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel: MovieViewModel by viewModels()

    private val args: SearchActivityArgs by navArgs()

    private var adapterSearchMovie = SearchAdapter(
        onItemClicked = {
            val intent = Intent(this@SearchActivity, DetailMovieActivity::class.java)
            intent.putExtra("movieID", it.id)
            startActivity(intent)
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setUpSearchView()

        val queryName = args.queryName
        binding.txtMovieSearch.text = queryName
        binding.searchView.queryHint = "Searching movie..."
        binding.searchView.setQuery(queryName, true)

        viewModel.searchMovieByName(queryName)
        viewModel.movieSearch.observe(this) {
            adapterSearchMovie.submitList(it)
            binding.recyclerSearchingMovie.adapter = adapterSearchMovie
            binding.recyclerSearchingMovie.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

        binding.btnBack.setOnClickListener {
            super.onBackPressed()
        }

    }

    private fun setUpSearchView() {

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.txtMovieSearch.text = query

                viewModel.searchMovieByName(query)
                viewModel.movieSearch.observe(this@SearchActivity) {
                    adapterSearchMovie.submitList(it)
                    binding.recyclerSearchingMovie.adapter = adapterSearchMovie
                    binding.recyclerSearchingMovie.layoutManager =
                        LinearLayoutManager(
                            this@SearchActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })


        binding.searchView.setOnSearchClickListener {
            binding.txtMovieSearch.visibility = View.INVISIBLE
        }

        binding.searchView.setOnCloseListener {
            binding.txtMovieSearch.visibility = View.VISIBLE
            false
        }
    }



}