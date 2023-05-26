package com.codewithshadow.filmrave.core.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithshadow.filmrave.R
import com.codewithshadow.filmrave.core.utils.Constants
import com.codewithshadow.filmrave.core.utils.DiffUtilCallback
import com.codewithshadow.filmrave.core.utils.isNetworkAvailable
import com.codewithshadow.filmrave.core.utils.loadImage
import com.codewithshadow.filmrave.core.utils.showToast
import com.codewithshadow.filmrave.databinding.ItemTopMovieBinding
import com.codewithshadow.filmrave.domain.model.MovieResult

class TopSearchesAdapter(
    private var onMovieClick: (movieResult: MovieResult) -> Unit
) : ListAdapter<MovieResult, TopSearchesAdapter.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemTopMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemTopMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieResponseResult: MovieResult) = binding.apply {
            val context = binding.root.context
            movieImage.loadImage(Constants.TMDB_IMAGE_BASE_URL_W500.plus(movieResponseResult.backdropPath))
            movieNameText.text = movieResponseResult.title
            root.setOnClickListener {
                if (isNetworkAvailable(binding.root.context)) {
                    onMovieClick(movieResponseResult)
                } else {
                    showToast(context, context.getString(R.string.internet_connection_required))
                }
            }
        }
    }
}
