package com.wahidabd.dicodingstories.data.network

import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PostService {

    @GET("stories")
    suspend fun getList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PostResponse

    @GET("stories")
    suspend fun getPostLocation(
        @Query("location") location: Int
    ): Response<PostResponse>

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part?,
        @Part("lat") lat: Float? = null,
        @Part("lon") lon: Float? = null
    ): Response<GenericResponse>

}