package com.wahidabd.dicodingstories.data.repository

import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.data.request.LoginRequest
import com.wahidabd.dicodingstories.data.request.PostRequest
import com.wahidabd.dicodingstories.data.request.RegisterRequest
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.LoginResponse
import com.wahidabd.dicodingstories.data.response.PostResponse
import com.wahidabd.dicodingstories.data.source.DicodingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DicodingRepository @Inject constructor(private val data: DicodingDataSource) {

    fun register(req: RegisterRequest): Flow<Resource<GenericResponse>> = data.register(req)
    fun login(req: LoginRequest): Flow<Resource<LoginResponse>> = data.login(req)

    fun getStories(): Flow<Resource<PostResponse>> = data.getStories()
    fun postStory(request: PostRequest): Flow<Resource<GenericResponse>> = data.postStory(request)

}