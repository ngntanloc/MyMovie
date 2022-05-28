package com.example.mymovie.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mymovie.R
import com.example.mymovie.databinding.DiscoveryItemBinding
import com.example.mymovie.databinding.PopularItemBinding
import com.example.mymovie.models.Movie
import com.example.mymovie.models.MovieItem
import com.example.mymovie.utils.Credentials

class ImageDiscoveryTopicAdapter(private val onItemClicked: (MovieItem) -> Unit) :
    ListAdapter<MovieItem, ImageDiscoveryTopicAdapter.ImageDiscoveryTopicViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageDiscoveryTopicViewHolder {
        return ImageDiscoveryTopicViewHolder(
            DiscoveryItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ImageDiscoveryTopicViewHolder, position: Int) {
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

    class ImageDiscoveryTopicViewHolder(private val binding: DiscoveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieItem: MovieItem) {
            with(movieItem) {
                val imgUrl = Credentials.GET_IMAGE + this.backdrop_path
                imgUrl.let {
                    val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                    binding.imgDiscovery.load(imgUri) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                }
            }
        }
    }

}