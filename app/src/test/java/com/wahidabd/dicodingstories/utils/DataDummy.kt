package com.wahidabd.dicodingstories.utils

import com.wahidabd.dicodingstories.data.model.PostModel
import com.wahidabd.dicodingstories.data.model.UserModel
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.LoginResponse
import com.wahidabd.dicodingstories.data.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

object DataDummy {

    fun generatePostsResponse(): PostResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<PostModel>()

        for (i in 0 until 10){
            val data = PostModel(
                id = "story-Nhq11P9eN5fnqjQ8",
                name =  "Abd. Wahid",
                description = "Test share location",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1661469343034_B1ouPS4e.jpg",
                createdAt = "2022-08-25T23:15:43.036Z",
                lat = -7.07769,
                lon = 112.85524
            )
            listStory.add(data)
        }
        return PostResponse(error, message, listStory)
    }

    fun generateListPost(): List<PostModel> {
        val items = arrayListOf<PostModel>()

        for (i in 0 until 10){
            val data = PostModel(
                id = "story-Nhq11P9eN5fnqjQ8",
                name =  "Abd. Wahid",
                description = "Test share location",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1661469343034_B1ouPS4e.jpg",
                createdAt = "2022-08-25T23:15:43.036Z",
                lat = -7.07769,
                lon = 112.85524
            )
            items.add(data)
        }

        return items
    }

    fun generateLoginResponse(): LoginResponse {
        val user = UserModel(
            userId = "user-NCwjLzvQGLrdaH2s",
            name = "Abd. Wahid",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU5Dd2pMenZRR0xyZGFIMnMiLCJpYXQiOjE2NjE0NzEyNzR9.B3u5kf8nYrYwx2Tftn5G8jDM4U5BR9HvNiDT9fZludA"
        )

        return LoginResponse(
            false,
            "success",
            user
        )
    }

    fun generateLoginResponseWithRetrofit(): Response<LoginResponse> {
        val user = UserModel(
            userId = "user-NCwjLzvQGLrdaH2s",
            name = "Abd. Wahid",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU5Dd2pMenZRR0xyZGFIMnMiLCJpYXQiOjE2NjE0NzEyNzR9.B3u5kf8nYrYwx2Tftn5G8jDM4U5BR9HvNiDT9fZludA"
        )

        return Response.success(LoginResponse(
            false,
            "success",
            user
        ))
    }


    fun generateRegisterResponse(): GenericResponse =
        GenericResponse(false, "success")

    fun generateMultipartFile(): MultipartBody.Part =
        MultipartBody.Part.create("text".toRequestBody())

    fun generateRequestBody(): RequestBody =
        "text".toRequestBody()

    fun generatePostStoryResponse(): GenericResponse =
        GenericResponse(false, "success")

    fun generateRegisterError(): GenericResponse =
        GenericResponse(true, "invalid email")

    fun generateLoginError(): GenericResponse =
        GenericResponse(true, "user not found")

    fun generateUploadError(): GenericResponse =
        GenericResponse(true, "UNKNOWN ERROR")
}