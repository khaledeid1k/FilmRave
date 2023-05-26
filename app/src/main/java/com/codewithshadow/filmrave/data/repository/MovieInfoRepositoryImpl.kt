package com.codewithshadow.filmrave.data.repository

import android.app.Application
import com.codewithshadow.filmrave.core.utils.Constants
import com.codewithshadow.filmrave.core.utils.NetworkResult
import com.codewithshadow.filmrave.core.utils.isNetworkAvailable
import com.codewithshadow.filmrave.data.local.LocalDataSource
import com.codewithshadow.filmrave.data.local.entity.MovieDataEntity
import com.codewithshadow.filmrave.data.model.HomeFeedResponse
import com.codewithshadow.filmrave.data.remote.RemoteDataSource
import com.codewithshadow.filmrave.domain.model.HomeFeedData
import com.codewithshadow.filmrave.domain.model.MovieList
import com.codewithshadow.filmrave.domain.model.MovieVideoList
import com.codewithshadow.filmrave.domain.repository.MovieInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException


class MovieInfoRepositoryImpl(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource,
    private val appContext: Application
) : MovieInfoRepository {

    // This function returns a Flow that emits the network result of fetching home feed data.
    override fun getHomeFeedData(): Flow<NetworkResult<HomeFeedData>> = flow {
        emit(NetworkResult.Loading())

        try {
            if (isNetworkAvailable(appContext)) {
                withContext(Dispatchers.IO) {
                    val nowPlayingMoviesListDef = async { remote.fetchNowPlayingMovies() }
                    val popularMoviesListDef = async { remote.fetchPopularMovies() }
                    val popularTvListDef = async { remote.fetchPopularTvShows() }
                    val topRatedMoviesListDef = async { remote.fetchTopRatedMovies() }
                    val animeSeriesDef = async { remote.fetchAnimeSeries() }
                    val bollywoodDef = async { remote.fetchBollywoodMovies() }
                    val netflixShowsDef = async { remote.fetchNetflixTvShows() }
                    val upcomingMovieDef = async { remote.fetchUpcomingMovies() }

                    val wholeList = mutableListOf<HomeFeedResponse>()

                    // Now playing
                    val nowPlayingMoviesList =
                        nowPlayingMoviesListDef.await()
                    // Popular Movies
                    val popularMoviesList = popularMoviesListDef.await()
                    // Popular Tv
                    val popularTvList = popularTvListDef.await()
                    // Top Rated
                    val topRatedMoviesList = topRatedMoviesListDef.await()
                    // Anime Series
                    val animeSeriesList = animeSeriesDef.await()
                    // Bollywood
                    val bollywoodList = bollywoodDef.await()
                    //Netflix Shows
                    val netflixShowsList = netflixShowsDef.await()
                    //Upcoming Movies
                    val upcomingMovies = upcomingMovieDef.await()

                    wholeList.add(
                        HomeFeedResponse(
                            Constants.UPCOMING_MOVIES,
                            upcomingMovies.body()?.results!!
                        )
                    )
                    wholeList.add(
                        HomeFeedResponse(
                            Constants.POPULAR_MOVIES,
                            popularMoviesList.body()?.results!!
                        )
                    )
                    wholeList.add(
                        HomeFeedResponse(
                            Constants.POPULAR_TV_SHOWS,
                            popularTvList.body()?.results!!
                        )
                    )
                    wholeList.add(
                        HomeFeedResponse(
                            Constants.TOP_RATED_MOVIES,
                            topRatedMoviesList.body()?.results!!
                        )
                    )
                    wholeList.add(
                        HomeFeedResponse(
                            Constants.ANIME_SERIES,
                            animeSeriesList.body()?.results!!
                        )
                    )
                    wholeList.add(
                        HomeFeedResponse(
                            Constants.BOLLYWOOD_MOVIES,
                            bollywoodList.body()?.results!!
                        )
                    )
                    wholeList.add(
                        HomeFeedResponse(
                            Constants.NETFLIX_SHOWS,
                            netflixShowsList.body()?.results!!
                        )
                    )

                    // Store the data in local database
                    val entity = MovieDataEntity(
                        bannerMovie = nowPlayingMoviesList.body()?.toMovieList()?.results!!,
                        homeFeedList = wholeList.map { it.toHomeFeed() }
                    )
                    local.deleteData()
                    local.insertData(entity)
                }
            }

            // Read the data from local database and convert it to HomeFeedData format
            val readData = local.readData().toHomeFeedDataInfo()

            // Emit the success state with the fetched data
            emit(
                NetworkResult.Success(
                    data = readData
                )
            )

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


    // This function returns a Flow that emits the network result of fetching movie recommendations data.
    override fun getMovieRecommendationsData(movieId: Int): Flow<NetworkResult<MovieList>> =
        flow {
            emit(NetworkResult.Loading())
            try {
                if (isNetworkAvailable(appContext)) {
                    val apiResponse = remote.fetchRecommendedMovies(movieId)

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


    // This function returns a Flow that emits the network result of fetching movie trailer data.
    override fun getMovieTrailerData(movieId: Int): Flow<NetworkResult<MovieVideoList>> = flow {
        emit(NetworkResult.Loading())
        try {
            if (isNetworkAvailable(appContext)) {
                val apiResponse = remote.fetchMovieVideo(movieId)
                // Emit the success state with the fetched data
                emit(NetworkResult.Success(apiResponse.body()?.toMovieVideoList()))
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