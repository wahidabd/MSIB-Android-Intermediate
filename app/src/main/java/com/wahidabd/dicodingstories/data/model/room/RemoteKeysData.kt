package com.wahidabd.dicodingstories.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_table")
data class  RemoteKeysData(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)