package com.wahidabd.dicodingstories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.data.model.PostModel
import com.wahidabd.dicodingstories.data.repository.DicodingRepository
import com.wahidabd.dicodingstories.data.repository.PostMediatorRepository
import com.wahidabd.dicodingstories.data.request.PostRequest
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.PostResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repo: DicodingRepository,
    private val repoMediator: PostMediatorRepository
) : ViewModel() {

    fun getList(): LiveData<PagingData<PostModel>> =
        repoMediator.getListPost().asLiveData().cachedIn(viewModelScope)

    fun getPosLocation(): LiveData<Resource<PostResponse>> =
        repo.getPostLocation().asLiveData()

    fun postStory(request: PostRequest, file: File): LiveData<Resource<GenericResponse>> =
        repo.postStory(request, file).asLiveData()

}