package com.wahidabd.dicodingstories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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

}