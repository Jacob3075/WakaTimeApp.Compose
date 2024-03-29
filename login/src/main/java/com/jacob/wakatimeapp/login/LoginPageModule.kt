package com.jacob.wakatimeapp.login

import com.jacob.wakatimeapp.login.data.LoginPageAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal object LoginPageModule {
    @Singleton
    @Provides
    fun provideLoginPageService(retrofit: Retrofit): LoginPageAPI =
        retrofit.create(LoginPageAPI::class.java)
}
