package com.codewithshadow.filmrave.domain.usecase

import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.domain.model.MovieList
import com.codewithshadow.filmrave.domain.repository.SearchInfoRepository
import kotlinx.coroutines.flow.Flow

class SearchListInfo(
    private val repository: SearchInfoRepository
) {

    fun searchMovieData(searchQuery: String): Flow<NetworkResult<MovieList>> {
        return repository.searchMovie(searchQuery)
    }

    fun getTrendingMovies(): Flow<NetworkResult<MovieList>> {
        return repository.trendingMovies()
    }
}