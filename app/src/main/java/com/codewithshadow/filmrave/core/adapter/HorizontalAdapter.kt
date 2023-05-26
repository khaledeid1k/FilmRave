package com.codewithshadow.filmrave.core.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithshadow.filmrave.R
import com.codewithshadow.filmrave.core.utils.Constants.TMDB_POSTER_IMAGE_BASE_URL_W342
import com.codewithshadow.filmrave.core.utils.DiffUtilCallback
import com.codewithshadow.filmrave.core.utils.isNetworkAvailable
import com.codewithshadow.filmrave.core.utils.loadImage
import com.codewithshadow.filmrave.core.utils.showToast
import com.codewithshadow.filmrave.databinding.ItemPosterBinding
import com.codewithshadow.filmrave.domain.model.MovieResult

class HorizontalAdapter(
    private var onPosterClick: ((movieResult: MovieResult) -> Unit)? = null
) : ListAdapter<MovieResult, HorizontalAdapter.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemPosterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieResponseResult: MovieResult) = binding.apply {
            val context = binding.root.context
            posterImage.loadImage(TMDB_POSTER_IMAGE_BASE_URL_W342.plus(movieResponseResult.posterPath))
            ratingText.text = String.format("%.1f", movieResponseResult.voteAverage)
            posterImage.setOnClickListener {
                if (isNetworkAvailable(binding.root.context)) {
                    onPosterClick?.invoke(movieResponseResult)
                } else {
                    showToast(context, context.getString(R.string.internet_connection_required))
                }
            }
        }
    }
}
