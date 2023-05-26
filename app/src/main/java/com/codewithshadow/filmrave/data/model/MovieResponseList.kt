package com.codewithshadow.filmrave.data.model

import com.codewithshadow.filmrave.domain.model.MovieList

data class MovieResponseList(
    val page: Int?,
    val results: List<MovieResponseResult>?,
    val total_pages: Int?,
    val total_results: Int?
) {
    fun toMovieList(): MovieList {
        return MovieList(results?.map { it.toMovieResult() })
    }
}