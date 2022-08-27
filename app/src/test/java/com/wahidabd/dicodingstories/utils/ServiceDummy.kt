package com.wahidabd.dicodingstories.utils

import com.wahidabd.dicodingstories.data.network.PostService
import com.wahidabd.dicodingstories.data.network.UserService
import com.wahidabd.dicodingstories.data.request.LoginRequest
import com.wahidabd.dicodingstories.data.request.RegisterRequest
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.LoginResponse
import com.wahidabd.dicodingstories.data.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ServiceDummy : UserService, PostService {

    override suspend fun getList(page: Int, size: Int): PostResponse =
        DataDummy.generatePostsResponse()

    override suspend fun getPostLocation(location: Int): Response<PostResponse> =
        Response.success(DataDummy.generatePostsResponse())

    override suspend fun postStory(
        description: RequestBody,
        file: MultipartBody.Part?,
        lat: Float?,
        lon: Float?
    ): Response<GenericResponse> =
        Response.success(DataDummy.generatePostStoryResponse())

    override suspend fun register(request: RegisterRequest): Response<GenericResponse> =
        Response.success(DataDummy.generateRegisterResponse())

    override suspend fun login(request: LoginRequest): Response<LoginResponse> =
        Response.success(DataDummy.generateLoginResponse())

    fun registerError(): GenericResponse =
        DataDummy.generateRegisterError()

    fun loginError(): GenericResponse =
        DataDummy.generateLoginError()

    fun uploadError(): GenericResponse =
        DataDummy.generateUploadError()
}