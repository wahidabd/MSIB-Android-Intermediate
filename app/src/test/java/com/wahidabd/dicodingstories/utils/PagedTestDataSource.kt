package com.wahidabd.dicodingstories.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wahidabd.dicodingstories.data.model.PostModel

class PagedTestDataSource : PagingSource<Int, LiveData<List<PostModel>>>() {

    companion object{
        fun snapshot(items: List<PostModel>): PagingData<PostModel> =
            PagingData.from(items)
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<PostModel>>>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<PostModel>>> =
        LoadResult.Page(emptyList(), 0, 1)
}