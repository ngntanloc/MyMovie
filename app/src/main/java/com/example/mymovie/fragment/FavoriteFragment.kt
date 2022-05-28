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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovie.R
import com.example.mymovie.adapter.FavoriteAdapter
import com.example.mymovie.databinding.FragmentFavoriteBinding
import com.example.mymovie.models.MovieFavorite
import com.example.mymovie.viewmodels.MovieViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    private val viewModel: MovieViewModel by activityViewModels()

    private lateinit var cm: ConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteAdapter = FavoriteAdapter(
            onItemClicked = {
                if (!isNetworkAvailable()) {
                    showNetworkDialogError()
                } else {
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailMovieActivity(movieId = it.id)
                    findNavController().navigate(action)
                }
            },
            onItemLongClicked = {
                showAlertDialogConfirmDelete(it)
            }
        )

        viewModel.favoriteMovie.observe(viewLifecycleOwner) {
            favoriteAdapter.submitList(it)
            Log.d("TAG", "onViewCreated: ${it.size}")
            binding.apply {
                recyclerViewNewMovie.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                recyclerViewNewMovie.adapter = favoriteAdapter
            }
        }
    }

    private fun showAlertDialogConfirmDelete(it: MovieFavorite) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_content, it.title))
            .setCancelable(true)
            .setNegativeButton(getString(R.string.disagree)) {_,_ -> }
            .setPositiveButton(getString(R.string.agree)) {_, _ ->
                viewModel.removeFavoriteMovie(it)
                Toast.makeText(requireContext(), "${it.title} removed", Toast.LENGTH_SHORT).show()
            }
            .show()
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