package com.codewithshadow.filmrave.data.model

import com.codewithshadow.filmrave.domain.model.MovieResult

data class MovieResponseResult(
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    val id: Int?,
    val original_language: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val vote_average: Double?,
) {
    fun toMovieResult(): MovieResult {
        return MovieResult(
            backdropPath = backdrop_path,
            title = title,
            id = id,
            voteAverage = vote_average,
            posterPath = poster_path,
            overview = overview,
            releaseDate = release_date,
            genreIds = genre_ids,
            originalLanguage = original_language
        )
    }
}