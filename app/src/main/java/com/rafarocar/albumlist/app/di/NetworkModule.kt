package com.rafarocar.albumlist.app.di

import android.content.Context
import coil.ImageLoader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module to provide the network dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    const val ALBUM_URL = "https://static.leboncoin.fr/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().also { builder ->
            builder
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header("User-Agent", "AlbumListApp/1.0")
                        .build()
                    chain.proceed(request)
                }
        }.build()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(ALBUM_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideImageLoader(
        @ApplicationContext context: Context,
        client: OkHttpClient
    ): ImageLoader =
        ImageLoader.Builder(context)
            .okHttpClient { client }
            .build()
}