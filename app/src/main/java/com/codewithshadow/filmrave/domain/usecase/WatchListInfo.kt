package com.codewithshadow.filmrave.domain.usecase

import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.data.local.entity.WatchListEntity
import com.codewithshadow.filmrave.domain.model.WatchProviders
import com.codewithshadow.filmrave.domain.repository.WatchListInfoRepository
import kotlinx.coroutines.flow.Flow

class WatchListInfo(
    private val repository: WatchListInfoRepository
) {
    fun getWatchListInfo(): Flow<List<WatchListEntity>> {
        return repository.getWatchListInfo()
    }

    suspend fun insertWatchListInfo(watchListEntity: WatchListEntity) {
        repository.insertWatchListInfo(watchListEntity)
    }

    suspend fun deleteWatchListInfo(watchListEntity: WatchListEntity) {
        repository.deleteWatchListInfo(watchListEntity)
    }

    fun getMovieWatchProviders(movieId: Int): Flow<NetworkResult<WatchProviders>> {
        return repository.getMovieWatchProviders(movieId)
    }
}