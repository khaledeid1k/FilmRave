package com.codewithshadow.filmrave.data.remote

import com.codewithshadow.filmrave.core.network.ApiClient
import com.codewithshadow.filmrave.data.model.MovieResponseList
import com.codewithshadow.filmrave.data.model.MovieResponseVideoList
import com.codewithshadow.filmrave.data.model.WatchProvidersResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun fetchPopularMovies(): Response<MovieResponseList> {
        return apiClient.fetchPopularMoviesApiCall()
    }

    suspend fun fetchNowPlayingMovies(): Response<MovieResponseList> {
        return apiClient.fetchNowPlayingMoviesApiCall()
    }


    suspend fun fetchTopRatedMovies(): Response<MovieResponseList> {
        return apiClient.fetchTopRatedMoviesApiCall()
    }


    suspend fun fetchPopularTvShows(): Response<MovieResponseList> {
        return apiClient.fetchPopularTvShowsApiCall()
    }


    suspend fun fetchNetflixTvShows(): Response<MovieResponseList> {
        return apiClient.fetchNetflixTvShowsApiCall()
    }

    suspend fun fetchUpcomingMovies(): Response<MovieResponseList> {
        return apiClient.fetchUpcomingMoviesApiCall()
    }

    suspend fun fetchAnimeSeries(): Response<MovieResponseList> {
        return apiClient.fetchAnimeSeriesApiCall()
    }


    suspend fun fetchBollywoodMovies(): Response<MovieResponseList> {
        return apiClient.fetchBollywoodMoviesApiCall()
    }

    suspend fun fetchRecommendedMovies(movieId: Int): Response<MovieResponseList> {
        return apiClient.fetchRecommendedMoviesApiCall(movieId)
    }


    suspend fun fetchMovieSearchedResults(query: String): Response<MovieResponseList> {
        return apiClient.fetchMovieSearchedResultsApiCall(searchQuery = query)
    }

    suspend fun fetchTrending(): Response<MovieResponseList> {
        return apiClient.fetchTrendingApiCall()
    }

    suspend fun fetchMovieVideo(movieId: Int): Response<MovieResponseVideoList> {
        return apiClient.fetchMovieVideoApiCall(movieId)
    }

    suspend fun fetchMovieWatchProviders(movieId: Int): Response<WatchProvidersResponse> {
        return apiClient.getMovieWatchProvidersApiCall(movieId)
    }

}