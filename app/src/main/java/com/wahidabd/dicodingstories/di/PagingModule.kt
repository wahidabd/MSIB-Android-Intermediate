package com.wahidabd.dicodingstories.di

import android.content.Context
import androidx.paging.RemoteMediator
import com.wahidabd.dicodingstories.data.network.PostService
import com.wahidabd.dicodingstories.data.repository.PostMediatorRepository
import com.wahidabd.dicodingstories.database.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [AppModule::class])
@InstallIn(SingletonComponent::class)
object PagingModule {

    @Provides
    @Singleton
    fun provideMyDatabase(@ApplicationContext context: Context) =
        MyDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun providePostDao(db: MyDatabase) = db.postDao()

    @Provides
    @Singleton
    fun provideRemoteKeysDao(db: MyDatabase) = db.remoteKeysDao()

    @Provides
    @Singleton
    fun providePostMediatorRepository(db: MyDatabase, service: PostService) =
        PostMediatorRepository(db, service)
}