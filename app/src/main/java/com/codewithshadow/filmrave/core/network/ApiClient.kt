package com.codewithshadow.filmrave.core.network

import com.codewithshadow.filmrave.data.model.MovieResponseList
import com.codewithshadow.filmrave.data.model.MovieResponseVideoList
import com.codewithshadow.filmrave.data.model.WatchProvidersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET("3/movie/popular")
    suspend fun fetchPopularMoviesApiCall(
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseList>


    @GET("3/movie/now_playing")
    suspend fun fetchNowPlayingMoviesApiCall(
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseList>

    @GET("3/movie/top_rated")
    suspend fun fetchTopRatedMoviesApiCall(
        @Query("region") region: String = "IN",
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseList>

    @GET("3/tv/popular")
    suspend fun fetchPopularTvShowsApiCall(
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseList>

    @GET("3/discover/tv")
    suspend fun fetchNetflixTvShowsApiCall(
        @Query("language") lang: String? = "en-US",
        @Query("with_networks") page: String = "213" // network code for NetFlix
    ): Response<MovieResponseList>

    @GET("3/movie/upcoming")
    suspend fun fetchUpcomingMoviesApiCall(
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String = "US"
    ): Response<MovieResponseList>


    @GET("3/discover/tv")
    suspend fun fetchAnimeSeriesApiCall(
        @Query("with_genres") genres: String = "16", // Animation genre = "16"
        @Query("sort_by") sortBy: String? = "popularity.desc",
        @Query("first_air_date.gte") firstAirDateGreaterThan: String = "2010-01-01",
        @Query("page") page: Int = 1,
        @Query("language") lang: String? = "en-US",
        @Query("with_original_language") origLang: String = "en",
        @Query("include_null_first_air_dates") include: Boolean = false
    ): Response<MovieResponseList>


    @GET("3/discover/movie")
    suspend fun fetchBollywoodMoviesApiCall(
        @Query("sort_by") sortBy: String? = "popularity.desc",
        @Query("primary_release_date.gte") releaseDateGreaterThan: String = "2012-08-01",
        @Query("page") page: Int = 1,
        @Query("region") region: String = "IN",
        @Query("with_release_type") releaseType: String = "3|2",
        @Query("watch_region") watchRegion: String = "IN",
        @Query("language") lang: String? = "hi-IN",
        @Query("with_original_language") origLang: String = "hi",
    ): Response<MovieResponseList>


    @GET("3/movie/{movie_id}/recommendations")
    suspend fun fetchRecommendedMoviesApiCall(
        @Path("movie_id") movieId: Int,
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseList>

    @GET("3/trending/{media_type}/{time_window}")
    suspend fun fetchTrendingApiCall(
        @Path("media_type") mediaType: String = "movie", // movie, tv, person, all
        @Path("time_window") timeWindow: String = "day", // day, week
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseList>

    @GET("3/search/movie")
    suspend fun fetchMovieSearchedResultsApiCall(
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("query") searchQuery: String
    ): Response<MovieResponseList>


    @GET("3/movie/{movie_id}/videos")
    suspend fun fetchMovieVideoApiCall(
        @Path("movie_id") movieId: Int,
        @Query("language") lang: String? = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseVideoList>


    @GET("3/movie/{movie_id}/watch/providers")
    suspend fun getMovieWatchProvidersApiCall(
        @Path("movie_id") movieId: Int,
    ): Response<WatchProvidersResponse>

}