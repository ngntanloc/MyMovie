package com.example.mymovie.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovie.R
import com.example.mymovie.adapter.ImageUpcomingTopicAdapter
import com.example.mymovie.adapter.PopularMovieItemAdapter
import com.example.mymovie.databinding.FragmentUpcomingBinding
import com.example.mymovie.viewmodels.MovieViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpcomingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpcomingFragment : Fragment() {

    private lateinit var binding: FragmentUpcomingBinding

    private val viewModel: MovieViewModel by activityViewModels()

    private lateinit var cm: ConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSearchView()


        val upcomingTopicImageAdapter = ImageUpcomingTopicAdapter(
            onItemClicked = {
                if (!isNetworkAvailable()) {
                    showNetworkDialogError()
                } else {
                    val action =
                        UpcomingFragmentDirections.actionUpcomingFragmentToDetailMovieActivity(
                            movieId = it.id
                        )
                    findNavController().navigate(action)
                }
            }
        )

        val upcomingItemAdapter = PopularMovieItemAdapter(
            onItemClicked = {
                if (!isNetworkAvailable()) {
                    showNetworkDialogError()
                } else {
                    val action =
                        UpcomingFragmentDirections.actionUpcomingFragmentToDetailMovieActivity(
                            movieId = it.id
                        )
                    findNavController().navigate(action)
                }
            }
        )

        viewModel.moviesUpcoming.observe(viewLifecycleOwner) {
            upcomingTopicImageAdapter.submitList(it)
            upcomingItemAdapter.submitList(it)

            binding.apply {
                binding.recyclerTopicUpcoming.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerTopicUpcoming.hasFixedSize()
                binding.recyclerTopicUpcoming.adapter = upcomingTopicImageAdapter

                binding.recyclerUpcomingMovie.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.recyclerUpcomingMovie.hasFixedSize()
                binding.recyclerUpcomingMovie.adapter = upcomingItemAdapter
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
                        UpcomingFragmentDirections.actionUpcomingFragmentToSearchActivity(queryName = query)
                    findNavController().navigate(action)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.searchView.setOnSearchClickListener {
            binding.upcomingTitle.visibility = View.INVISIBLE
        }

        binding.searchView.setOnCloseListener {
            binding.upcomingTitle.visibility = View.VISIBLE
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
        cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
    }

}