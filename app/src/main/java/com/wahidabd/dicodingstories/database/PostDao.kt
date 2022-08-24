package com.wahidabd.dicodingstories.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wahidabd.dicodingstories.data.model.PostModel

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(data: List<PostModel>)

    @Query("SELECT * FROM post_table")
    fun getPostList(): PagingSource<Int, PostModel>

    @Query("DELETE FROM post_table")
    suspend fun deleteAllPost()
}