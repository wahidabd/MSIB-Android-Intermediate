package com.wahidabd.dicodingstories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.data.repository.DicodingRepository
import com.wahidabd.dicodingstories.data.request.PostRequest
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.PostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val repo: DicodingRepository) : ViewModel() {

    fun getStories(): LiveData<Resource<PostResponse>> =
        repo.getStories().asLiveData()

    fun postStory(request: PostRequest): LiveData<Resource<GenericResponse>> =
        repo.postStory(request).asLiveData()

}