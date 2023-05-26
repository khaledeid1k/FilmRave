package com.codewithshadow.filmrave.data.local

import com.codewithshadow.filmrave.data.local.entity.MovieDataEntity
import com.codewithshadow.filmrave.data.local.entity.WatchListEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val userDataDao: MovieDataDao
) {

    suspend fun readData(): MovieDataEntity {
        return userDataDao.readData()
    }

    suspend fun insertData(dataModel: MovieDataEntity) {
        userDataDao.insertData(dataModel)
    }

    suspend fun deleteData() {
        userDataDao.deleteData()
    }

    suspend fun insertWatchListData(watchListEntity: WatchListEntity) {
        userDataDao.insertWatchListData(watchListEntity)
    }

    suspend fun readWatchListData(): List<WatchListEntity> {
        return userDataDao.readWatchListData()
    }

    suspend fun deleteWatchListData(watchListEntity: WatchListEntity) {
        userDataDao.deleteWatchListData(watchListEntity)
    }
}