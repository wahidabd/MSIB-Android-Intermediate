package com.wahidabd.dicodingstories.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.core.Status
import com.wahidabd.dicodingstories.data.request.LoginRequest
import com.wahidabd.dicodingstories.data.request.RegisterRequest
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.LoginResponse
import com.wahidabd.dicodingstories.utils.CoroutinesTestRule
import com.wahidabd.dicodingstories.utils.DataDummy
import com.wahidabd.dicodingstories.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)

class AuthViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val loginResponse = DataDummy.generateLoginResponse()

    @Mock
    lateinit var viewModel: AuthViewModel

    @Test
    fun `User login - success`() = runTest {
        val expectedResponse = loginResponse
        val data = MutableLiveData<Resource<LoginResponse>>()
        data.value = Resource.success(expectedResponse)

        val request = LoginRequest("email@mail.com", "password")
        `when`(viewModel.login(request)).thenReturn(data)
        val actual = viewModel.login(request).getOrAwaitValue()
        verify(viewModel).login(request)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(actual.data, expectedResponse)
    }

    @Test
    fun `User login - error`() = runTest {
        val data = MutableLiveData<Resource<LoginResponse>>()
        data.value = Resource.error("UNKNOWN ERROR", null)

        val request = LoginRequest("email@mail.com", "password")
        `when`(viewModel.login(request)).thenReturn(data)
        val actual = viewModel.login(request).getOrAwaitValue()
        verify(viewModel).login(request)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(data.value, actual)
    }

    @Test
    fun `User register - success`() = runTest {
        val expectedResult = DataDummy.generateRegisterResponse()
        val request = RegisterRequest("name", "email@mail.com", "password")
        val data = MutableLiveData<Resource<GenericResponse>>()
        data.value = Resource.success(expectedResult)

        `when`(viewModel.register(request)).thenReturn(data)
        val actual = viewModel.register(request).getOrAwaitValue()
        verify(viewModel).register(request)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(actual.data, expectedResult)
    }

    @Test
    fun `User register - error`() = runTest {
        val request = RegisterRequest("name", "email@mail.com", "password")
        val data = MutableLiveData<Resource<GenericResponse>>()
        data.value = Resource.error("UNKNOWN ERROR", null)

        `when`(viewModel.register(request)).thenReturn(data)
        val actual = viewModel.register(request).getOrAwaitValue()
        verify(viewModel).register(request)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(data.value, actual)
    }

}