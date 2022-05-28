package com.example.mymovie.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mymovie.R
import com.example.mymovie.databinding.UpcomingItemBinding
import com.example.mymovie.models.MovieItem
import com.example.mymovie.utils.Credentials

class ImageUpcomingTopicAdapter(private val onItemClicked: (MovieItem) -> Unit) : ListAdapter<MovieItem, ImageUpcomingTopicAdapter.ImageUpcomingViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageUpcomingViewHolder {
        return ImageUpcomingViewHolder(
            UpcomingItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ImageUpcomingViewHolder, position: Int) {
        val movieItem = getItem(position)
        holder.bind(movieItem)
        holder.itemView.setOnClickListener {
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

    class ImageUpcomingViewHolder(private val binding: UpcomingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieItem: MovieItem) {
            with(movieItem) {
                val imgUrl = Credentials.GET_IMAGE + this.poster_path
                imgUrl.let {
                    val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                    binding.imgUpcoming.load(imgUri) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                }
            }
        }
    }

}