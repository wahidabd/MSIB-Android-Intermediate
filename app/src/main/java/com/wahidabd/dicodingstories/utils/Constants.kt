package com.wahidabd.dicodingstories.utils

import android.Manifest

object Constants {
    const val UNKNOWN_ERROR = "Unknown error occurred, please try again later."
    const val TIMEOUT_ERROR = "The server took too long to respond, please try again later."

    const val THEME_SETTING = "theme_settings"

    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    const val REQUEST_CODE_PERMISSIONS = 10

    const val SPLASH_DURATION: Long = 1500

    const val STARTING_PAGE_INDEX = 1
}