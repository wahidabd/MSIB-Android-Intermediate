package com.wahidabd.dicodingstories.utils

import android.Manifest

object Constants {
    const val UNKNOWN_ERROR = "Unknown error occurred, please try again later."
    const val TIMEOUT_ERROR = "The server took too long to respond, please try again later."

    const val THEME_SETTING = "theme_settings"

    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    val LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    const val REQUEST_CODE_PERMISSIONS = 10

    const val SPLASH_DURATION: Long = 1500

    const val STARTING_PAGE_INDEX = 1

    var BASE_URL_MOCK: String? = null
}