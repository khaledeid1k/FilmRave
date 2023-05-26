package com.codewithshadow.filmrave.presentation.watchlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.data.local.entity.WatchListEntity
import com.codewithshadow.filmrave.domain.model.MovieResult
import com.codewithshadow.filmrave.domain.model.WatchProviders
import com.codewithshadow.filmrave.domain.usecase.WatchListInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val watchListInfo: WatchListInfo

) : ViewModel() {

    /** ROOM DATABASE */
    private var _watchList: MutableLiveData<List<WatchListEntity>> = MutableLiveData()
    var watchList: LiveData<List<WatchListEntity>> = _watchList

    private var _watchListProviders: MutableLiveData<NetworkResult<WatchProviders>> =
        MutableLiveData()
    var watchListProviders: LiveData<NetworkResult<WatchProviders>> = _watchListProviders

    fun getWatchListInfoData() {
        viewModelScope.launch {
            watchListInfo.getWatchListInfo().onEach { result ->
                _watchList.value = result
            }.launchIn(this)
        }
    }

    fun getWatchListProviders(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            watchListInfo.getMovieWatchProviders(movieId).onEach { result ->
                _watchListProviders.postValue(result)
            }.launchIn(this)
        }
    }


    fun insertWatchListInfoData(movieResponseResult: MovieResult) {
        val watchListEntity = WatchListEntity(movieResponseResult, movieResponseResult.id!!)
        viewModelScope.launch(Dispatchers.IO) {
            watchListInfo.insertWatchListInfo(watchListEntity)
        }
    }


    fun deleteWatchListInfoData(movieResponseResult: MovieResult) {
        val watchListEntity = WatchListEntity(movieResponseResult, movieResponseResult.id!!)
        viewModelScope.launch(Dispatchers.IO) {
            watchListInfo.deleteWatchListInfo(watchListEntity)
        }
    }


}