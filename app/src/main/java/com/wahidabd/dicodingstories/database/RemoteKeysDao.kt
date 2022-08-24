package com.wahidabd.dicodingstories.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wahidabd.dicodingstories.data.model.room.RemoteKeysData
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteKeysDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysData>)

    @Query("SELECT * FROM remote_keys_table WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeysData?

    @Query("DELETE FROM remote_keys_table")
    suspend fun deleteRemoteKeys()

}