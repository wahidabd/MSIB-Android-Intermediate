package com.wahidabd.dicodingstories.data.model

data class UserModel(
    val userId: String,
    val name: String,
    val email: String? = null,
    val token: String,
)