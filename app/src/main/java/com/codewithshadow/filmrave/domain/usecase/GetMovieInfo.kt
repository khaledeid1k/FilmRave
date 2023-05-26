package com.codewithshadow.filmrave.domain.usecase

import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.domain.model.HomeFeedData
import com.codewithshadow.filmrave.domain.model.MovieList
import com.codewithshadow.filmrave.domain.model.MovieVideoList
import com.codewithshadow.filmrave.domain.repository.MovieInfoRepository
import kotlinx.coroutines.flow.Flow

class GetMovieInfo(
    private val repository: MovieInfoRepository
) {
    fun getHomeFeedData(): Flow<NetworkResult<HomeFeedData>> {
        return repository.getHomeFeedData()
    }

    fun getMovieRecommendationsData(movieId: Int): Flow<NetworkResult<MovieList>> {
        return repository.getMovieRecommendationsData(movieId)
    }

    fun getMovieTrailerData(movieId: Int): Flow<NetworkResult<MovieVideoList>> {
        return repository.getMovieTrailerData(movieId)
    }
}