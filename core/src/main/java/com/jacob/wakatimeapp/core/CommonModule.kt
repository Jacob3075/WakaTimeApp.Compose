package com.jacob.wakatimeapp.core

import com.jacob.wakatimeapp.core.models.UserSession
import com.jacob.wakatimeapp.core.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.networkInterceptors().add(loggingInterceptor)
        return builder.build()
    }

    @ExperimentalSerializationApi
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideUserSession() = UserSession()

    @Singleton
    @Provides
    fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO
}