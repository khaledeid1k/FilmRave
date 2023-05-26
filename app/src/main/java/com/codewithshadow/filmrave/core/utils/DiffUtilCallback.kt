package com.codewithshadow.filmrave.core.utils

import androidx.recyclerview.widget.DiffUtil
import com.codewithshadow.filmrave.domain.model.MovieResult

class DiffUtilCallback : DiffUtil.ItemCallback<MovieResult>() {

    override fun areItemsTheSame(
        oldItem: MovieResult,
        newItem: MovieResult
    ): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: MovieResult,
        newItem: MovieResult
    ): Boolean =
        oldItem == newItem
}