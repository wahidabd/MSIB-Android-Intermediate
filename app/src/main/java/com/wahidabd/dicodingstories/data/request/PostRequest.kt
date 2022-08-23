package com.wahidabd.dicodingstories.data.request

import java.io.File

data class PostRequest(
    val description: String,
    val file: File
)
