package com.etax.movieapp.di

import android.os.Build
import com.etax.movieapp.BuildConfig
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModuleProvider {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(
        moshi: Moshi,
    ): Converter.Factory {
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    @LoggerInterceptor
    fun provideLoggerInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    @AuthorizationInterceptor
    fun provideAuthorizationInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            val url = request.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.REMOTE_API_KEY)
                .build()

            chain.proceed(
                request.newBuilder()
                    .url(url)
                    .addHeader("accept", "application/json")
                    .build()
            )
        }
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(
        @LoggerInterceptor loggerInterceptor: Interceptor,
        @AuthorizationInterceptor headerInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggerInterceptor)
            .addInterceptor(headerInterceptor)
            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(converterFactory)
            .build()
    }

}

private const val CONNECTION_TIME_OUT = 10000L
private const val READ_TIME_OUT = 5000L


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthorizationInterceptor

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LoggerInterceptor
