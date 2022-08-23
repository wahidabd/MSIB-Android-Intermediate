package com.wahidabd.dicodingstories.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
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

    companion object{
        private val THEME_KEY = booleanPreferencesKey("theme_setting")

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