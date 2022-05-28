package com.example.mymovie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mymovie.R
import com.example.mymovie.databinding.FavoriteItemBinding
import com.example.mymovie.databinding.SearchItemBinding
import com.example.mymovie.models.MovieFavorite
import com.example.mymovie.models.MovieSearch
import com.example.mymovie.models.MovieSearchItem
import com.example.mymovie.utils.Credentials

class SearchAdapter(
    private val onItemClicked: (MovieSearchItem) -> Unit
) : ListAdapter<MovieSearchItem, SearchAdapter.FavoriteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            SearchItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movieItem = getItem(position)
        holder.bind(movieItem)
        holder.itemView.setOnClickListener {
            onItemClicked(movieItem)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MovieSearchItem>() {
        override fun areItemsTheSame(oldItem: MovieSearchItem, newItem: MovieSearchItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieSearchItem, newItem: MovieSearchItem): Boolean {
            return oldItem.title == newItem.title
        }
    }

    class FavoriteViewHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieSearchItem: MovieSearchItem) {

            val imgUrl = Credentials.GET_IMAGE + movieSearchItem.poster_path
            imgUrl.let {
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                binding.imgSearch.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }

            binding.apply {
                txtSearchTitle.text =
                    itemView.resources.getString(R.string.title_favorite, movieSearchItem.title)

                txtReleaseDate.text = itemView.resources.getString(
                    R.string.release_date,
                    movieSearchItem.release_date
                )

                txtImdb.text = itemView.resources.getString(
                    R.string.imdb_point_favorite,
                    movieSearchItem.vote_average.toString()
                )
            }
        }
    }
}