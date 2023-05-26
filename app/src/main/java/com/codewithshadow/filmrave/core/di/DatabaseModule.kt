package com.codewithshadow.filmrave.core.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import com.codewithshadow.filmrave.core.utils.Constants.DATABASE_NAME
import com.codewithshadow.filmrave.core.utils.GsonParser
import com.codewithshadow.filmrave.data.local.LocalDataSource
import com.codewithshadow.filmrave.data.local.MovieDataDao
import com.codewithshadow.filmrave.data.local.MovieDataTypeConverter
import com.codewithshadow.filmrave.data.local.MovieInfoDatabase
import com.codewithshadow.filmrave.data.remote.RemoteDataSource
import com.codewithshadow.filmrave.data.repository.MovieInfoRepositoryImpl
import com.codewithshadow.filmrave.data.repository.WatchListInfoRepositoryImpl
import com.codewithshadow.filmrave.domain.repository.MovieInfoRepository
import com.codewithshadow.filmrave.domain.repository.WatchListInfoRepository
import com.codewithshadow.filmrave.domain.usecase.GetMovieInfo
import com.codewithshadow.filmrave.domain.usecase.WatchListInfo
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovieInfoDatabase {
        return Room.databaseBuilder(
            context,
            MovieInfoDatabase::class.java,
            DATABASE_NAME
        ).addTypeConverter(MovieDataTypeConverter(GsonParser(Gson())))
            .build()
    }


    @Singleton
    @Provides
    fun provideGetMovieInfoUseCase(repository: MovieInfoRepository): GetMovieInfo {
        return GetMovieInfo(repository)
    }

    @Singleton
    @Provides
    fun provideMovieInfoRepository(
        remote: RemoteDataSource,
        local: LocalDataSource,
        appContext: Application
    ): MovieInfoRepository {
        return MovieInfoRepositoryImpl(remote, local, appContext)
    }


    @Singleton
    @Provides
    fun provideWatchListInfoUseCase(repository: WatchListInfoRepository): WatchListInfo {
        return WatchListInfo(repository)
    }

    @Singleton
    @Provides
    fun provideWatchListInfoRepository(
        remote: RemoteDataSource,
        local: LocalDataSource,
        appContext: Application
    ): WatchListInfoRepository {
        return WatchListInfoRepositoryImpl(remote, local, appContext)
    }


    @Singleton
    @Provides
    fun provideDao(database: MovieInfoDatabase): MovieDataDao {
        return database.dao
    }
}