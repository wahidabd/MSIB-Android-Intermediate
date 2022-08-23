package com.wahidabd.dicodingstories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.data.repository.DicodingRepository
import com.wahidabd.dicodingstories.data.request.LoginRequest
import com.wahidabd.dicodingstories.data.request.RegisterRequest
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: DicodingRepository) : ViewModel() {

    fun register(req: RegisterRequest): LiveData<Resource<GenericResponse>> =
        repo.register(req).asLiveData()

    fun login(req: LoginRequest): LiveData<Resource<LoginResponse>> =
        repo.login(req).asLiveData()

}