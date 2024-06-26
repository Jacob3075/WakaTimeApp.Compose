package com.jacob.wakatimeapp.core.common.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.jacob.wakatimeapp.core.common.auth.AuthDataStore.Companion.STORE_NAME
import com.jacob.wakatimeapp.core.common.data.local.AppDatabase
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Duration
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    @Suppress("InjectDispatcher")
    fun providePreferencesDataStore(@ApplicationContext appContext: Context) =
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(STORE_NAME) },
        )

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(Duration.ofMinutes(1))
            .connectTimeout(Duration.ofMinutes(1))

//        builder.networkInterceptors().add(HttpLoggingInterceptor().setLevel(BODY))

        return builder.build()
    }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    internal fun provideApplicationDatabase(@ApplicationContext applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "wakatime.db",
    ).build()

    @Singleton
    @Provides
    fun provideWakaTimeDb(appDatabase: AppDatabase) = appDatabase.applicationDao()

    private const val BASE_URL = "https://wakatime.com/"
}
