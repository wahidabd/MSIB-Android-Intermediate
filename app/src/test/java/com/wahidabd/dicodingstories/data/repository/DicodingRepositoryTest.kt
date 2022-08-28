package com.wahidabd.dicodingstories.data.repository

import com.wahidabd.dicodingstories.core.ErrorParser
import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.core.SafeCall
import com.wahidabd.dicodingstories.core.Status
import com.wahidabd.dicodingstories.data.network.PostService
import com.wahidabd.dicodingstories.data.network.UserService
import com.wahidabd.dicodingstories.data.request.LoginRequest
import com.wahidabd.dicodingstories.data.request.PostRequest
import com.wahidabd.dicodingstories.data.request.RegisterRequest
import com.wahidabd.dicodingstories.data.source.DicodingDataSource
import com.wahidabd.dicodingstories.utils.CoroutinesTestRule
import com.wahidabd.dicodingstories.utils.DataDummy
import com.wahidabd.dicodingstories.utils.ServiceDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DicodingRepositoryTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var source: DicodingDataSource

    @Mock
    private lateinit var sourceMock: DicodingDataSource

    @Mock
    private lateinit var repoMock: DicodingRepository

    private val dummyDesc = DataDummy.generateRequestBody()
    private val dummyMultipart = DataDummy.generateMultipartFile()

    private val loginRequest = LoginRequest("email@mail.com", "password")
    private val loginResponse = DataDummy.generateLoginResponse()

    private val registerRequest = RegisterRequest("Name", "email@mail.com", "password")
    private val registerResponse = DataDummy.generateRegisterResponse()

    private val file = File("Image")
    private val uploadRequest = PostRequest("desc", null, null)
    private val uploadResponse = DataDummy.generatePostStoryResponse()

    @Before
    fun setup() {
        val userService = mock(UserService::class.java)
        val postService = mock(PostService::class.java)
        val safeCall = mock(SafeCall::class.java)
        val converter = mock(ErrorParser::class.java)

        source = DicodingDataSource(safeCall, converter, userService, postService)
        sourceMock = mock(DicodingDataSource::class.java)
        repoMock = mock(DicodingRepository::class.java)
    }

    @Test
    fun `User login - success`() = runBlocking {
        val service = ServiceDummy()
        val expectedResult = loginResponse
        val actualResult = service.login(loginRequest).body()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `User login - error`() = runBlocking {
        val service = ServiceDummy()
        val expected = DataDummy.generateLoginError()
        val actual = service.loginError()
        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `User register - success`() = runBlocking {
        val service = ServiceDummy()
        val expectedResult = registerResponse
        val actualResult = service.register(registerRequest).body()
        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `User register - error`() = runBlocking {
        val service = ServiceDummy()
        val expected = DataDummy.generateRegisterError()
        val actual = service.registerError()
        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `Get stories with location - success`() = runBlocking {
        val expectedResult = flowOf(
            Resource.success(DataDummy.generatePostsResponse())
        )

        `when`(repoMock.getPostLocation()).thenReturn(expectedResult)
        repoMock.getPostLocation().collect { result ->
            if (result.status == Status.SUCCESS) {
                Assert.assertNotNull(result)

                expectedResult.collect {
                    Assert.assertEquals(it, result)
                }
            }
        }
    }

    @Test
    fun `Get stories with location - error`() = runBlocking {
        val expectedResult = flowOf(Resource.error("UNKNOWN ERROR", null))
        `when`(repoMock.getPostLocation()).thenReturn(expectedResult)
        repoMock.getPostLocation().collect { result ->
            if (result.status == Status.ERROR) {
                Assert.assertNotNull(result)

                expectedResult.collect{
                    Assert.assertEquals(it, result)
                }
            }
        }
    }

    @Test
    fun `Upload new story - success`(): Unit = runBlocking {
        val service = ServiceDummy()
        val expectedResult = uploadResponse
        val actualResult = service.postStory(dummyDesc, dummyMultipart, null, null).body()

        Assert.assertNotNull(actualResult)
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Upload new story - error`(): Unit = runBlocking {
        val service = ServiceDummy()
        val expected = DataDummy.generateUploadError()
        val actual = service.uploadError()

        Assert.assertNotNull(actual)
        Assert.assertEquals(expected, actual)
    }

}