package com.wahidabd.dicodingstories.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.wahidabd.dicodingstories.data.model.PostModel
import com.wahidabd.dicodingstories.data.model.room.RemoteKeysData
import com.wahidabd.dicodingstories.data.network.PostService
import com.wahidabd.dicodingstories.database.MyDatabase
import com.wahidabd.dicodingstories.utils.Constants.STARTING_PAGE_INDEX
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val db: MyDatabase,
    private val service: PostService
) : RemoteMediator<Int, PostModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostModel>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyCloseToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKetForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKetForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        return try {
            val response = service.getList(page, state.config.pageSize)
            val responseData = response.listStory
            val endOfPagination = response.listStory.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().deleteRemoteKeys()
                    db.postDao().deleteAllPost()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val keys = responseData.map {
                    RemoteKeysData(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                db.postDao().insertPost(responseData)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction =
        InitializeAction.LAUNCH_INITIAL_REFRESH

    private suspend fun getRemoteKetForLastItem(state: PagingState<Int, PostModel>): RemoteKeysData? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            db.remoteKeysDao().getRemoteKeysId(data.id)
        }

    private suspend fun getRemoteKetForFirstItem(state: PagingState<Int, PostModel>): RemoteKeysData? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            db.remoteKeysDao().getRemoteKeysId(data.id)
        }

    private suspend fun getRemoteKeyCloseToCurrentPosition(state: PagingState<Int, PostModel>): RemoteKeysData? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.remoteKeysDao().getRemoteKeysId(id)
            }
        }
}