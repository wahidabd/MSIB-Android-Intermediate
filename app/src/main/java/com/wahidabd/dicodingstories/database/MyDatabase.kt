package com.wahidabd.dicodingstories.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wahidabd.dicodingstories.data.model.PostModel
import com.wahidabd.dicodingstories.data.model.room.RemoteKeysData

@Database(entities = [PostModel::class, RemoteKeysData::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object{
        @Volatile
        private var instance: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, MyDatabase::class.java, "dicoding.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }

}