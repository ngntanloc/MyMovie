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
import com.example.mymovie.models.MovieFavorite
import com.example.mymovie.utils.Credentials

class FavoriteAdapter(
    private val onItemClicked: (MovieFavorite) -> Unit,
    private val onItemLongClicked: (MovieFavorite) -> Unit
) : ListAdapter<MovieFavorite, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            FavoriteItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movieItem = getItem(position)
        holder.bind(movieItem)
        holder.itemView.setOnClickListener {
            onItemClicked(movieItem)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClicked(movieItem)
            true
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MovieFavorite>() {
        override fun areItemsTheSame(oldItem: MovieFavorite, newItem: MovieFavorite): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieFavorite, newItem: MovieFavorite): Boolean {
            return oldItem.title == newItem.title
        }
    }

    class FavoriteViewHolder(private val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieFavorite: MovieFavorite) {

            val imgUrl = Credentials.GET_IMAGE + movieFavorite.poster_path
            imgUrl.let {
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                binding.imgFavorite.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }

            binding.apply {
                txtFavoriteTitle.text =
                    itemView.resources.getString(R.string.title_favorite, movieFavorite.title)
                if (movieFavorite.genre2 == null) {
                    txtGenres.text = itemView.resources.getString(
                        R.string.genre_favorite,
                        movieFavorite.genre1,
                        "",
                        ""
                    )
                } else {
                    txtGenres.text = itemView.resources.getString(
                        R.string.genre_favorite,
                        movieFavorite.genre1,
                        "|",
                        movieFavorite.genre2
                    )
                }

                txtImdb.text = itemView.resources.getString(
                    R.string.imdb_point_favorite,
                    movieFavorite.vote_average.toString()
                )
            }
        }
    }
}