package com.wahidabd.dicodingstories.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreference(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.THEME_SETTING)
    private val dataStore = context.dataStore

    fun getTheme(): Flow<Boolean> =
        dataStore.data.map { pref ->
            pref[THEME_KEY] ?: false
        }

    suspend fun setTheme(darkMode: Boolean){
        dataStore.edit { pref ->
            pref[THEME_KEY] = darkMode
        }
    }

    fun getMapStyle(): Flow<MapStyle> =
        dataStore.data.map { pref ->
            when(pref[MAP_STYLE]){
                MapStyle.NORMAL.name -> MapStyle.NORMAL
                MapStyle.NIGHT.name -> MapStyle.NIGHT
                MapStyle.SILVER.name -> MapStyle.SILVER
                else -> MapStyle.NORMAL
            }
        }

    suspend fun setMapStyle(data: MapStyle){
        dataStore.edit { pref ->
            pref[MAP_STYLE] = when(data){
                MapStyle.NORMAL -> MapStyle.NORMAL.name
                MapStyle.NIGHT -> MapStyle.NIGHT.name
                MapStyle.SILVER -> MapStyle.SILVER.name
            }
        }
    }

    fun getMapType(): Flow<MapType> =
        dataStore.data.map { pref ->
            when(pref[MAP_TYPE]){
                MapType.NORMAL.name -> MapType.NORMAL
                MapType.SATTELITE.name -> MapType.SATTELITE
                MapType.TERRAIN.name -> MapType.TERRAIN
                else -> MapType.NORMAL
            }
        }

    suspend fun setMapType(data: MapType){
        dataStore.edit { pref ->
            pref[MAP_TYPE] = when(data){
                MapType.NORMAL -> MapType.NORMAL.name
                MapType.SATTELITE -> MapType.SATTELITE.name
                MapType.TERRAIN -> MapType.TERRAIN.name
            }
        }
    }

    companion object{
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val MAP_STYLE = stringPreferencesKey("map_style")
        private val MAP_TYPE = stringPreferencesKey("map_type")

        @Volatile
        private var instance: SettingPreference? = null

        fun getInstance(context: Context): SettingPreference =
            instance ?: synchronized(this){
                instance ?: SettingPreference(context).also {
                    instance = it
                }
            }
    }

}