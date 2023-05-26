package com.codewithshadow.filmrave.data.repository

import android.app.Application
import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.core.utils.isNetworkAvailable
import com.codewithshadow.filmrave.data.remote.RemoteDataSource
import com.codewithshadow.filmrave.domain.model.MovieList
import com.codewithshadow.filmrave.domain.repository.SearchInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class SearchInfoRepositoryImpl(
    private val remote: RemoteDataSource,
    private val appContext: Application
) : SearchInfoRepository {

    // This function returns a Flow that emits the network result of fetching searched movie data.
    override fun searchMovie(searchQuery: String): Flow<NetworkResult<MovieList>> = flow {
        emit(NetworkResult.Loading())
        try {
            if (isNetworkAvailable(appContext)) {
                val apiResponse = remote.fetchMovieSearchedResults(searchQuery)

                // Emit the success state with the fetched data
                emit(NetworkResult.Success(apiResponse.body()?.toMovieList()))
            }
        } catch (throwable: Throwable) {
            when (throwable) {

                is IOException -> {
                    emit(
                        NetworkResult.Error(
                            "Please check your internet connection"
                        )
                    )
                }

                else -> {
                    emit(
                        NetworkResult.Error(
                            throwable.message ?: "Something went wrong"
                        )
                    )
                }
            }
        }
    }

    // This function returns a Flow that emits the network result of fetching trending movies data.
    override fun trendingMovies(): Flow<NetworkResult<MovieList>> = flow {
        emit(NetworkResult.Loading())
        try {
            if (isNetworkAvailable(appContext)) {
                val apiResponse = remote.fetchTrending()
                // Emit the success state with the fetched data
                emit(NetworkResult.Success(apiResponse.body()?.toMovieList()))
            }
        } catch (throwable: Throwable) {
            when (throwable) {

                is IOException -> {
                    emit(
                        NetworkResult.Error(
                            "Please check your internet connection"
                        )
                    )
                }

                else -> {
                    emit(
                        NetworkResult.Error(
                            throwable.message ?: "Something went wrong"
                        )
                    )
                }
            }
        }
    }
}