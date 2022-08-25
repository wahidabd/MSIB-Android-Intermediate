package com.wahidabd.dicodingstories.data.request

data class PostRequest(
    val description: String,
    val lat: Float? = null,
    val lon: Float? = null
)
