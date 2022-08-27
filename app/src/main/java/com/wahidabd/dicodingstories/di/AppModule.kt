package com.wahidabd.dicodingstories.di

import android.content.Context
import com.wahidabd.dicodingstories.BuildConfig.BASE_URL
import com.wahidabd.dicodingstories.BuildConfig.DEBUG
import com.wahidabd.dicodingstories.core.ErrorParser
import com.wahidabd.dicodingstories.utils.MySharedPreference
import com.wahidabd.dicodingstories.core.SafeCall
import com.wahidabd.dicodingstories.data.network.PostService
import com.wahidabd.dicodingstories.data.network.UserService
import com.wahidabd.dicodingstories.data.network.connection.JwtInterceptor
import com.wahidabd.dicodingstories.data.repository.DicodingRepository
import com.wahidabd.dicodingstories.data.source.DicodingDataSource
import com.wahidabd.dicodingstories.utils.Constants.BASE_URL_MOCK
import com.wahidabd.dicodingstories.utils.SettingPreference
import com.wahidabd.dicodingstories.utils.lottie.LottieLoading
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLottieLoading() = LottieLoading()

    @Provides
    @Singleton
    fun provideSafeCall() = SafeCall()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) =
        SettingPreference.getInstance(context)

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context) =
        MySharedPreference(context)

    @Provides
    @Singleton
    fun provideOkHttpInterceptor() = HttpLoggingInterceptor().apply {
        level = if (DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        prefs: MySharedPreference
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(JwtInterceptor(prefs))
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL_MOCK ?: BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providePostService(retrofit: Retrofit): PostService =
        retrofit.create(PostService::class.java)

    @Provides
    @Singleton
    fun provideErrorParser(retrofit: Retrofit) = ErrorParser(retrofit)

    @Provides
    @Singleton
    fun provideDicodingDataSource(
        safeCall: SafeCall,
        converter: ErrorParser,
        userService: UserService,
        postService: PostService
    ) = DicodingDataSource(safeCall, converter, userService, postService)

    @Provides
    @Singleton
    fun provideDicodingRepository(data: DicodingDataSource) =
        DicodingRepository(data)
}