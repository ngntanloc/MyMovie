package com.example.mymovie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mymovie.R
import com.example.mymovie.databinding.CastItemBinding
import com.example.mymovie.models.Cast
import com.example.mymovie.models.MovieItem
import com.example.mymovie.utils.Credentials

class CastAdapter(private val onItemClicked: (Cast) -> Unit): ListAdapter<Cast, CastAdapter.CastViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(CastItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castItem = getItem(position)
        holder.bind(castItem)
        holder.itemView.setOnClickListener {
            onItemClicked(castItem)
        }
    }

    class CastViewHolder(private val binding: CastItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            binding.txtName.text = cast.name
            binding.txtCharacter.text = cast.character
            val imgUrl = Credentials.GET_IMAGE + cast.profile_path
            imgUrl.let {
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                binding.imgCast.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            }
        }
    }
}