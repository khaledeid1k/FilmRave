package com.codewithshadow.filmrave.data.model

import com.codewithshadow.filmrave.domain.model.MovieVideoList

data class MovieResponseVideoList(
    val id: Int?,
    val results: List<MovieResponseVideoResult>?
) {
    fun toMovieVideoList(): MovieVideoList {
        return MovieVideoList(id, results?.map { it.toMovieVideoResult() })
    }
}