package com.codewithshadow.filmrave.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithshadow.filmrave.R
import com.codewithshadow.filmrave.core.utils.Constants
import com.codewithshadow.filmrave.core.utils.isNetworkAvailable
import com.codewithshadow.filmrave.core.utils.loadImage
import com.codewithshadow.filmrave.core.utils.showToast
import com.codewithshadow.filmrave.data.local.entity.WatchListEntity
import com.codewithshadow.filmrave.databinding.ItemPosterBinding
import com.codewithshadow.filmrave.domain.model.MovieResult

class WatchListAdapter(
    private var onPosterClick: ((movieResult: MovieResult) -> Unit)? = null
) : ListAdapter<WatchListEntity, WatchListAdapter.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemPosterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieResult: WatchListEntity) = binding.apply {
            val item = movieResult.bannerMovie
            val context = binding.root.context
            posterImage.loadImage(Constants.TMDB_POSTER_IMAGE_BASE_URL_W342.plus(item.posterPath))
            ratingText.text = String.format("%.1f", item.voteAverage)
            posterImage.setOnClickListener {
                if (isNetworkAvailable(binding.root.context)) {
                    onPosterClick?.invoke(item)
                } else {
                    showToast(context, context.getString(R.string.internet_connection_required))
                }
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<WatchListEntity>() {

        override fun areItemsTheSame(oldItem: WatchListEntity, newItem: WatchListEntity): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: WatchListEntity,
            newItem: WatchListEntity
        ): Boolean =
            oldItem == newItem
    }
}