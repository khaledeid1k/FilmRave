package com.codewithshadow.filmrave.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codewithshadow.filmrave.core.utils.Constants
import com.codewithshadow.filmrave.domain.model.HomeFeed
import com.codewithshadow.filmrave.domain.model.HomeFeedData
import com.codewithshadow.filmrave.domain.model.MovieResult

@Entity(tableName = Constants.TABLE_NAME)
data class MovieDataEntity(
    val homeFeedList: List<HomeFeed>,
    val bannerMovie: List<MovieResult>,
    @PrimaryKey val id: Int? = null
) {

    fun toHomeFeedDataInfo(): HomeFeedData {
        return HomeFeedData(
            bannerMovie = bannerMovie,
            homeFeedResponseList = homeFeedList
        )
    }

}