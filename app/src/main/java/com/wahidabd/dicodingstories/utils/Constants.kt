package com.wahidabd.dicodingstories.utils

import android.Manifest
import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {
    const val UNKNOWN_ERROR = "Unknown error occurred, please try again later."
    const val TIMEOUT_ERROR = "The server took too long to respond, please try again later."
    const val CONNECTIVITY_ERROR = "No connection detected."

    const val THEME_SETTING = "theme_settings"

    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    const val REQUEST_CODE_PERMISSIONS = 10
}