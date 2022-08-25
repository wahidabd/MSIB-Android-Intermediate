package com.wahidabd.dicodingstories.data.source

import com.wahidabd.dicodingstories.core.ErrorParser
import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.core.SafeCall
import com.wahidabd.dicodingstories.data.network.PostService
import com.wahidabd.dicodingstories.data.network.UserService
import com.wahidabd.dicodingstories.data.request.LoginRequest
import com.wahidabd.dicodingstories.data.request.PostRequest
import com.wahidabd.dicodingstories.data.request.RegisterRequest
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.LoginResponse
import com.wahidabd.dicodingstories.data.response.PostResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class DicodingDataSource @Inject constructor(
    private val safeCall: SafeCall,
    private val converter: ErrorParser,
    private val userService: UserService,
    private val postService: PostService
) {

    fun register(req: RegisterRequest): Flow<Resource<GenericResponse>> = flow {
        emit(Resource.loading(null))

        val res = safeCall.enqueue(req, converter::converterGenericError, userService::register)
        emit(res)
    }.flowOn(Dispatchers.IO)

    fun login(req: LoginRequest): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.loading(null))

        val res = safeCall.enqueue(req, converter::converterGenericError, userService::login)
        emit(res)
    }.flowOn(Dispatchers.IO)

    fun getPostLocation(): Flow<Resource<PostResponse>> = flow {
        emit(Resource.loading(null))

        val res = safeCall.enqueue(1, converter::converterGenericError, postService::getPostLocation)
        emit(res)
    }.flowOn(Dispatchers.IO)

    fun postStory(request: PostRequest): Flow<Resource<GenericResponse>> = flow {
        emit(Resource.loading(null))

        val desc = request.description.toRequestBody("text/plain".toMediaType())
        val imageFile = request.file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("photo", request.file.name, imageFile)

        val res = safeCall.enqueue(desc, imageMultipart, converter::converterGenericError, postService::postStory)
        emit(res)
    }.flowOn(Dispatchers.IO)

}