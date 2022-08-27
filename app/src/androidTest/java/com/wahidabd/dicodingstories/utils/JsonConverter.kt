package com.wahidabd.dicodingstories.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import java.io.InputStreamReader
import java.lang.Exception

object JsonConverter {
    fun readStringFromFile(file: String): String =
        try {
            val applicationContext = ApplicationProvider.getApplicationContext<Context>()
            val inputStream = applicationContext.assets.open(file)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            builder.toString()
        }catch (e: Exception){
            throw e
        }
}