package com.wahidabd.dicodingstories.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wahidabd.dicodingstories.data.model.PostModel
import com.wahidabd.dicodingstories.data.network.PostService
import com.wahidabd.dicodingstories.data.source.PostRemoteMediator
import com.wahidabd.dicodingstories.database.MyDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostMediatorRepository @Inject constructor(
    private val db: MyDatabase,
    private val service: PostService
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getListPost(): Flow<PagingData<PostModel>> = Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = PostRemoteMediator(db, service) ,
            pagingSourceFactory = {db.postDao().getPostList()}
        ).flow

}