package com.codewithshadow.filmrave.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codewithshadow.filmrave.core.utils.Constants.TMDB_IMAGE_BASE_URL_W780
import com.codewithshadow.filmrave.core.utils.DiffUtilCallback
import com.codewithshadow.filmrave.core.utils.getMovieGenreListFromIds
import com.codewithshadow.filmrave.core.utils.loadImage
import com.codewithshadow.filmrave.databinding.ItemBannerBinding
import com.codewithshadow.filmrave.domain.model.MovieResult

class BannerAdapter(
) : ListAdapter<MovieResult, BannerAdapter.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieResponseResult: MovieResult) = binding.apply {

            binding.bannerTitle.text = movieResponseResult.title
            binding.bannerGenres.text = getMovieGenreListFromIds(
                movieResponseResult.genreIds
            ).joinToString(" • ") { it.name }

            bannerImage.loadImage(TMDB_IMAGE_BASE_URL_W780.plus(movieResponseResult.backdropPath))

        }
    }
}
