package com.example.mymovie.adapter

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mymovie.R
import com.example.mymovie.databinding.PopularItemBinding
import com.example.mymovie.models.Movie
import com.example.mymovie.models.MovieItem
import com.example.mymovie.utils.Credentials

class PopularMovieItemAdapter(private val onItemClicked: (MovieItem) -> Unit) :
    ListAdapter<MovieItem, PopularMovieItemAdapter.PopularMovieItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieItemViewHolder {
        return PopularMovieItemViewHolder(
            PopularItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: PopularMovieItemViewHolder, position: Int) {
        val movieItem = getItem(position)
        holder.bind(movieItem)
        holder.itemView.setOnClickListener{
            onItemClicked(movieItem)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.poster_path == newItem.poster_path
        }
    }

    class PopularMovieItemViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieItem: MovieItem) {
            with(movieItem) {
                val imgUrl = Credentials.GET_IMAGE + this.poster_path
                imgUrl.let {
                    val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                    binding.imagePopularMovie.load(imgUri) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                }
                binding.txtPopularMovieName.text = itemView.context.resources.getString(R.string.movie_title, movieItem.title)
            }
        }
    }

}