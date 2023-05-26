package com.codewithshadow.filmrave.domain.repository

import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.data.local.entity.WatchListEntity
import com.codewithshadow.filmrave.domain.model.WatchProviders
import kotlinx.coroutines.flow.Flow

interface WatchListInfoRepository {

    fun getWatchListInfo(): Flow<List<WatchListEntity>>
    suspend fun insertWatchListInfo(watchListEntity: WatchListEntity)

    suspend fun deleteWatchListInfo(watchListEntity: WatchListEntity)

    fun getMovieWatchProviders(movieId: Int): Flow<NetworkResult<WatchProviders>>

}