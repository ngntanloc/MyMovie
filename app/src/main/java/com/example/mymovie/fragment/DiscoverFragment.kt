package com.example.mymovie.fragment

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovie.R
import com.example.mymovie.adapter.ImageDiscoveryTopicAdapter
import com.example.mymovie.adapter.PopularMovieItemAdapter
import com.example.mymovie.databinding.FragmentDiscoverBinding
import com.example.mymovie.viewmodels.MovieViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFragment : Fragment() {

    //    private val viewModel: MovieViewModel by activityViewModels()
    private val viewModel: MovieViewModel by activityViewModels()

    private lateinit var binding: FragmentDiscoverBinding

    private lateinit var cm: ConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSearchView()

        val imageTopicAdapter = ImageDiscoveryTopicAdapter(
            onItemClicked = {
                if (!isNetworkAvailable()) {
                    showNetworkDialogError()
                } else {
                    val action =
                        DiscoverFragmentDirections.actionDiscoverFragmentToDetailMovieActivity(
                            movieId = it.id
                        )
                    findNavController().navigate(action)
                }
            }
        )

        val popularMovieItemAdapter = PopularMovieItemAdapter(
            onItemClicked = {

                if (!isNetworkAvailable()) {
                    showNetworkDialogError()
                } else {
                    val action =
                        DiscoverFragmentDirections.actionDiscoverFragmentToDetailMovieActivity(
                            movieId = it.id
                        )
                    findNavController().navigate(action)
                }
            }
        )


        viewModel.movieDetailInformation.observe(viewLifecycleOwner) {
            Log.d("TAG", "onViewCreated: ${viewModel.movieDetailInformation.value?.original_title}")
        }

        viewModel.moviesPopular.observe(viewLifecycleOwner) {
            imageTopicAdapter.submitList(it)
            popularMovieItemAdapter.submitList(it)

            binding.apply {
                binding.recyclerTopicImage.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerTopicImage.adapter = imageTopicAdapter

                binding.recyclerViewNewMovie.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.recyclerTopicImage.hasFixedSize()
                binding.recyclerViewNewMovie.adapter = popularMovieItemAdapter
            }
        }
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!isNetworkAvailable()) {
                    showNetworkDialogError()
                } else {
                    val action =
                        DiscoverFragmentDirections.actionDiscoverFragmentToSearchActivity(queryName = query)
                    findNavController().navigate(action)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.searchView.setOnSearchClickListener {
            binding.discoveryTitle.visibility = INVISIBLE
        }

        binding.searchView.setOnCloseListener {
            binding.discoveryTitle.visibility = VISIBLE
            false
        }
    }

    private fun showNetworkDialogError() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.network_error_title))
            .setMessage(getString(R.string.network_error_content))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.confirm_message)) { _, _ -> }
            .show()
    }

    private fun isNetworkAvailable(): Boolean {
        cm = context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return (capabilities != null && capabilities.hasCapability(NET_CAPABILITY_INTERNET))
    }


}

