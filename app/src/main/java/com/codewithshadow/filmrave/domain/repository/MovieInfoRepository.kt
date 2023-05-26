package com.codewithshadow.filmrave.domain.repository

import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.domain.model.HomeFeedData
import com.codewithshadow.filmrave.domain.model.MovieList
import com.codewithshadow.filmrave.domain.model.MovieVideoList
import kotlinx.coroutines.flow.Flow

interface MovieInfoRepository {

    fun getHomeFeedData(): Flow<NetworkResult<HomeFeedData>>
    fun getMovieRecommendationsData(movieId:Int): Flow<NetworkResult<MovieList>>
    fun getMovieTrailerData(movieId:Int): Flow<NetworkResult<MovieVideoList>>

}