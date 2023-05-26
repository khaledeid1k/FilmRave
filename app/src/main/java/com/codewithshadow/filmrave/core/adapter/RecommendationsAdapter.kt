package com.codewithshadow.filmrave.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithshadow.filmrave.core.utils.Constants
import com.codewithshadow.filmrave.core.utils.DiffUtilCallback
import com.codewithshadow.filmrave.core.utils.isNetworkAvailable
import com.codewithshadow.filmrave.core.utils.loadImage
import com.codewithshadow.filmrave.databinding.ItemPosterBinding
import com.codewithshadow.filmrave.domain.model.MovieResult

class RecommendationsAdapter(
    private var onPosterClick: ((movieResult: MovieResult) -> Unit)
) : ListAdapter<MovieResult, RecommendationsAdapter.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val binding: ItemPosterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieResult: MovieResult) = binding.apply {
            posterImage.loadImage(Constants.TMDB_POSTER_IMAGE_BASE_URL_W342.plus(movieResult.posterPath))
            ratingText.text = String.format("%.1f", movieResult.voteAverage)
            root.setOnClickListener {
                if (isNetworkAvailable(binding.root.context)) {
                    onPosterClick.invoke(movieResult)
                }
            }
        }
    }
}