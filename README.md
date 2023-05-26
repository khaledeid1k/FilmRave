
# FilmRave 🍿🎬

![App Screenshot](https://github.com/SakshamSharma2026/FilmRave/blob/main/assets/app_banner.png?raw=true)

Welcome to filmRave, the ultimate movie app for film lovers! With filmRave, you can easily discover new movies, manage your watchlist, and get detailed information about your favorite films, all in one convenient place.

Using [The Movie Database (TMDB) API,](https://www.themoviedb.org/documentation/api) filmRave fetches up-to-date information about movies, including ratings, recommendations, movie trailers and much more. With a user-friendly dashboard, you can quickly access the most popular and trending movies, as well as new releases and upcoming films. You can also explore movies by genre or search for specific titles.

With the watchlist feature, you can keep track of the movies you want to see by adding in the watchlist section. FilmRave also offers similar movie recommendations based on your viewing history, so you can easily find new movies that match your interests.

Even if you're without internet access, filmRave has got you covered with offline support. You can access your watchlist and recently viewed movies, even without an internet connection.

In addition to the clean and intuitive user interface, filmRave also provides detailed information about each movie, including trailers and clips, and other related content. The YouTube player integration allows you to watch trailers and other related content right within the app.

With filmRave, you'll never miss a great movie again.

This project follows the MVVM structure with Clean Architecture.


## Feature 💪🏻

- Dashboard
- Explore
- WatchList
- Similar Recommendations
- Offline Support
- No Internet Handling
- Clean UI
- Detailed Info
- YouTube Player


## Folder Structure 🗂️

    ├── FilmRaveApp.kt
    ├── core
    │   ├── adapter
    │   │   ├── BannerAdapter.kt
    │   │   ├── HomeAdapter.kt
    │   │   ├── HorizontalAdapter.kt
    │   │   ├── RecommendationsAdapter.kt
    │   │   ├── TopSearchesAdapter.kt
    │   │   └── WatchListAdapter.kt
    │   ├── di
    │   │   ├── DatabaseModule.kt
    │   │   └── NetworkModule.kt
    │   ├── network
    │   │   └── ApiClient.kt
    │   └── utils
    │       ├── Constants.kt
    │       ├── DatastorePreferences.kt
    │       ├── DiffUtilCallback.kt
    │       ├── ExtensionUtil.kt
    │       ├── GsonParser.kt
    │       ├── JsonParser.kt
    │       ├── NetworkResult.kt
    │       └── ViewUtils.kt
    ├── data
    │   ├── local
    │   │   ├── LocalDataSource.kt
    │   │   ├── MovieDataDao.kt
    │   │   ├── MovieDataTypeConverter.kt
    │   │   ├── MovieInfoDatabase.kt
    │   │   └── entity
    │   │       ├── MovieDataEntity.kt
    │   │       └── WatchListEntity.kt
    │   ├── model
    │   │   ├── HomeFeedResponse.kt
    │   │   ├── MovieResponseList.kt
    │   │   ├── MovieResponseResult.kt
    │   │   ├── MovieResponseVideoList.kt
    │   │   ├── MovieResponseVideoResult.kt
    │   │   └── WatchProvidersResponse.kt
    │   ├── remote
    │   │   └── RemoteDataSource.kt
    │   └── repository
    │       ├── MovieInfoRepositoryImpl.kt
    │       ├── SearchInfoRepositoryImpl.kt
    │       └── WatchListInfoRepositoryImpl.kt
    ├── domain
    │   ├── model
    │   │   ├── Genre.kt
    │   │   ├── HomeFeed.kt
    │   │   ├── MovieList.kt
    │   │   ├── MovieResult.kt
    │   │   ├── MovieVideoList.kt
    │   │   ├── MovieVideoResult.kt
    │   │   └── WatchProviders.kt
    │   ├── repository
    │   │   ├── MovieInfoRepository.kt
    │   │   ├── SearchInfoRepository.kt
    │   │   └── WatchListInfoRepository.kt
    │   └── usecase
    │       ├── GetMovieInfo.kt
    │       ├── SearchListInfo.kt
    │       └── WatchListInfo.kt
    └── presentation
        ├── MainActivity.kt
        ├── base
        │   └── BaseFragment.kt
        ├── details
        │   └── DetailsFragment.kt
        ├── home
        │   ├── HomeFragment.kt
        │   └── MovieInfoViewModel.kt
        ├── intro
        │   └── IntroFragment.kt
        ├── search
        │   ├── SearchFragment.kt
        │   └── SearchViewModel.kt
        ├── splash
        │   └── SplashFragment.kt
        └── watchlist
            ├── WatchListFragment.kt
            └── WatchListViewModel.kt

## Developed Using 👨🏻‍💻

- [Android Architecture Components](https://developer.android.com/topic/architecture) -Collection of libraries that help you design robust, testable, and maintainable apps.
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
- [ViewModel]() - Stores UI-related data that isn't destroyed on UI changes.
- [Dagger-Hilt](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - For asynchronous and more..
- [Flow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs. Basically it's a replacement for SharedPreferences.


## Note 

Add your TMDB API key in gradle.properties under movieApiKey section..