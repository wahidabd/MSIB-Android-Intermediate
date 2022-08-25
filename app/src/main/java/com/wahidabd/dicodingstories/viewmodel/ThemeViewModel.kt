package com.wahidabd.dicodingstories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wahidabd.dicodingstories.core.MapStyle
import com.wahidabd.dicodingstories.core.MapType
import com.wahidabd.dicodingstories.utils.SettingPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(private val pref: SettingPreference) : ViewModel() {

    fun getTheme(): LiveData<Boolean> =
        pref.getTheme().asLiveData()

    fun setTheme(darkMode: Boolean){
        viewModelScope.launch {
            pref.setTheme(darkMode)
        }
    }

    fun getMapStyle(): LiveData<MapStyle> =
        pref.getMapStyle().asLiveData()

    fun setMapStyle(data: MapStyle){
        viewModelScope.launch {
            pref.setMapStyle(data)
        }
    }

    fun getMapType(): LiveData<MapType> =
        pref.getMapType().asLiveData()

    fun setMapType(data: MapType){
        viewModelScope.launch {
            pref.setMapType(data)
        }
    }
}