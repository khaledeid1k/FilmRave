package com.codewithshadow.filmrave.domain.repository

import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.domain.model.MovieList
import kotlinx.coroutines.flow.Flow

interface SearchInfoRepository {

    fun searchMovie(searchQuery:String): Flow<NetworkResult<MovieList>>
    fun trendingMovies(): Flow<NetworkResult<MovieList>>


}