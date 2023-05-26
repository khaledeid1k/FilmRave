package com.codewithshadow.filmrave.data.model

import com.codewithshadow.filmrave.domain.model.HomeFeed
import com.codewithshadow.filmrave.domain.model.HomeFeedData

data class HomeFeedDataResponse(
    val bannerMovie: List<MovieResponseResult>,
    val homeFeedResponseList: List<HomeFeedResponse>
) {
    fun toHomeFeedData(): HomeFeedData {
        return HomeFeedData(
            bannerMovie = bannerMovie.map { it.toMovieResult() },
            homeFeedResponseList = homeFeedResponseList.map { it.toHomeFeed() })
    }
}

data class HomeFeedResponse(
    val title: String,
    val list: List<MovieResponseResult>
) {
    fun toHomeFeed(): HomeFeed {
        return HomeFeed(title = title, list = list.map { it.toMovieResult() })
    }
}
