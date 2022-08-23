package com.wahidabd.dicodingstories.data.response

import com.wahidabd.dicodingstories.data.model.UserModel

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: UserModel
)