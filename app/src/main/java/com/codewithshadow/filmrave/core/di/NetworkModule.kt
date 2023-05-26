package com.codewithshadow.filmrave.core.di

import android.app.Application
import android.content.Context
import com.codewithshadow.filmrave.BuildConfig
import com.codewithshadow.filmrave.core.network.ApiClient
import com.codewithshadow.filmrave.data.remote.RemoteDataSource
import com.codewithshadow.filmrave.data.repository.SearchInfoRepositoryImpl
import com.codewithshadow.filmrave.domain.repository.SearchInfoRepository
import com.codewithshadow.filmrave.domain.usecase.SearchListInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideNetworkClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient().newBuilder()
            .cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong()))
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url
                val url =
                    originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.MOVIE_API_KEY)
                        .build()
                request.url(url)
                return@addInterceptor chain.proceed(request.build())
            }
            .addInterceptor { chain ->
                var request = chain.request()
                request =
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 60 * 5)
                        .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient).addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Singleton
    @Provides
    fun provideSearchInfoRepository(
        remote: RemoteDataSource,
        appContext: Application
    ): SearchInfoRepository {
        return SearchInfoRepositoryImpl(remote, appContext)
    }


    @Singleton
    @Provides
    fun provideSearchListInfoUseCase(repository: SearchInfoRepository): SearchListInfo {
        return SearchListInfo(repository)
    }

    @Singleton
    @Provides
    fun provideApiServices(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }
}