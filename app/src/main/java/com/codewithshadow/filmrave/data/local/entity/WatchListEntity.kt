package com.codewithshadow.filmrave.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codewithshadow.filmrave.core.utils.Constants.WATCHLIST_TABLE_NAME
import com.codewithshadow.filmrave.domain.model.MovieResult

@Entity(tableName = WATCHLIST_TABLE_NAME)
data class WatchListEntity(
    val bannerMovie: MovieResult,
    @PrimaryKey val id: Int
)