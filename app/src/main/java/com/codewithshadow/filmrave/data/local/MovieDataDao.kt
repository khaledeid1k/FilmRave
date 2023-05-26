package com.codewithshadow.filmrave.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codewithshadow.filmrave.data.local.entity.MovieDataEntity
import com.codewithshadow.filmrave.data.local.entity.WatchListEntity

@Dao
interface MovieDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(movieDataEntity: MovieDataEntity)

    @Query("SELECT * FROM movie_data_table ORDER BY id ASC")
    suspend fun readData(): MovieDataEntity

    @Query("DELETE FROM movie_data_table")
    suspend fun deleteData()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchListData(watchListEntity: WatchListEntity)

    @Query("SELECT * FROM watch_list_news_table ORDER BY id DESC")
    suspend fun readWatchListData(): List<WatchListEntity>

    @Delete
    suspend fun deleteWatchListData(watchListEntity: WatchListEntity)

}