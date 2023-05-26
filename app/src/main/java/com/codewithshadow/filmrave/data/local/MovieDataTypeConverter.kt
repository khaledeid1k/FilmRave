package com.codewithshadow.filmrave.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.codewithshadow.filmrave.core.utils.JsonParser
import com.codewithshadow.filmrave.data.model.MovieResponseResult
import com.codewithshadow.filmrave.domain.model.HomeFeed
import com.codewithshadow.filmrave.domain.model.HomeFeedData
import com.codewithshadow.filmrave.domain.model.MovieResult
import com.google.gson.reflect.TypeToken


@ProvidedTypeConverter
class MovieDataTypeConverter(private val jsonParser: JsonParser) {

    @TypeConverter
    fun homeFeedDataListToString(homeFeedData: List<HomeFeedData>): String {
        val listType = object : TypeToken<ArrayList<HomeFeedData>>() {}.type
        return jsonParser.toJson(homeFeedData, listType) ?: "[]"
    }

    @TypeConverter
    fun stringToHomeFeedDataList(data: String): List<HomeFeedData> {
        val listType = object : TypeToken<ArrayList<HomeFeedData>>() {}.type
        return jsonParser.fromJson<ArrayList<HomeFeedData>>(
            data, listType
        ) ?: emptyList()
    }


    @TypeConverter
    fun movieResultListDataToStringData(movieResult: List<MovieResult>): String {
        val listType = object : TypeToken<ArrayList<MovieResponseResult>>() {}.type
        return jsonParser.toJson(movieResult, listType) ?: "[]"
    }

    @TypeConverter
    fun stringToMovieResultListData(data: String): List<MovieResult> {
        val listType = object : TypeToken<ArrayList<MovieResult>>() {}.type
        return jsonParser.fromJson<ArrayList<MovieResult>>(
            data, listType
        ) ?: emptyList()
    }


    @TypeConverter
    fun movieResultDataToStringData(movieResult: MovieResult): String {
        val listType = object : TypeToken<MovieResult>() {}.type
        return jsonParser.toJson(movieResult, listType) ?: "[]"
    }

    @TypeConverter
    fun stringToMovieResultData(data: String): MovieResult? {
        val listType = object : TypeToken<MovieResult>() {}.type
        return jsonParser.fromJson<MovieResult>(
            data, listType
        )
    }


    @TypeConverter
    fun dataToStringHomeFeedData(homeFeedData: List<HomeFeed>): String {
        val listType = object : TypeToken<ArrayList<HomeFeed>>() {}.type
        return jsonParser.toJson(homeFeedData, listType) ?: "[]"
    }

    @TypeConverter
    fun stringToHomeFeedData(data: String): List<HomeFeed> {
        val listType = object : TypeToken<ArrayList<HomeFeed>>() {}.type
        return jsonParser.fromJson<ArrayList<HomeFeed>>(
            data, listType
        ) ?: emptyList()
    }

}