package com.codewithshadow.filmrave.domain.model


data class HomeFeedData(
    val bannerMovie: List<MovieResult>,
    val homeFeedResponseList: List<HomeFeed>
)

data class HomeFeed(
    val title: String,
    val list: List<MovieResult>
)