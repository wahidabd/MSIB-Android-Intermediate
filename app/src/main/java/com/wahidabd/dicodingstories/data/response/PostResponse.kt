package com.wahidabd.dicodingstories.data.response

import com.wahidabd.dicodingstories.data.model.PostModel

data class PostResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<PostModel>
)