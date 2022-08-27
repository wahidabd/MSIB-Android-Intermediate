package com.wahidabd.dicodingstories.data.source

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.wahidabd.dicodingstories.core.ErrorParser
import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.core.SafeCall
import com.wahidabd.dicodingstories.data.network.PostService
import com.wahidabd.dicodingstories.data.network.UserService
import com.wahidabd.dicodingstories.data.repository.DicodingRepository
import com.wahidabd.dicodingstories.data.repository.PostMediatorRepository
import com.wahidabd.dicodingstories.data.request.LoginRequest
import com.wahidabd.dicodingstories.data.response.PostResponse
import com.wahidabd.dicodingstories.utils.CoroutinesTestRule
import com.wahidabd.dicodingstories.utils.DataDummy
import com.wahidabd.dicodingstories.utils.PagedTestDataSource
import com.wahidabd.dicodingstories.database.MyDatabase
import com.wahidabd.dicodingstories.view.page.home.PostPagingAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DicodingDataSourceTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyDatabase: MyDatabase

    private lateinit var safeCall: SafeCall
    private lateinit var converter: ErrorParser
    private lateinit var postService: PostService
    private lateinit var userService: UserService

    @Mock
    private lateinit var postMock: PostMediatorRepository

    @Mock
    private lateinit var sourceMock: DicodingDataSource
    @Mock
    private lateinit var source: DicodingDataSource
    private lateinit var repo: DicodingRepository

    private val multipart = DataDummy.generateMultipartFile()
    private val desc = DataDummy.generateRequestBody()
    private val storyResponse = DataDummy.generatePostsResponse()

    private val loginRequest = LoginRequest("wahidtest@test.id", "wahid123")

    @Before
    fun setup() {
        safeCall = mock(SafeCall::class.java)
        converter = mock(ErrorParser::class.java)
        userService = mock(UserService::class.java)
        postService = mock(PostService::class.java)
        source = DicodingDataSource(safeCall, converter, userService, postService)
        repo = DicodingRepository(source)
    }

    @Test
    fun `Login - success`() = runBlocking {

    }


    @Test
    fun `Get stories with paging - success`() = runTest {
        val story = DataDummy.generateListPost()
        val data = PagedTestDataSource.snapshot(story)

        val expectedResult = flowOf(data)
        `when`(postMock.getListPost()).thenReturn(expectedResult)

        postMock.getListPost().collect { actualResult ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = PostPagingAdapter.differCallback,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(actualResult)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(
                storyResponse.listStory.size,
                differ.snapshot().size
            )
        }
    }

    @Test
    fun `Get stories with location - success`() = runTest {
        val expectedResult = flowOf(Resource.success(storyResponse))

        `when`(sourceMock.getPostLocation()).thenReturn(expectedResult)

        sourceMock.getPostLocation().collect { res ->
            Assert.assertNotNull(res.data)
            Assert.assertEquals(storyResponse, res.data)
        }
    }

    @Test
    fun `Get stories with location - error`() = runTest {
        val expectedResult = flowOf<Resource<PostResponse>>(Resource.error("UNKNOWN ERROR", null))

        `when`(sourceMock.getPostLocation()).thenReturn(expectedResult)

        sourceMock.getPostLocation().collect { res ->
            Assert.assertNotNull(res)
            Assert.assertNotNull(res.message)
        }
    }

//    @Test
//    fun `Post story - success`() = runTest {
//        val expectedResponse = flowOf(Resource.success(DataDummy.generatePostStoryResponse()))
//        val postRequest = PostRequest(description = "text")
//        val fileName = "image"
//        val file = File(fileName)
//
//        `when`(
//            safeCall.enqueue(
//                desc,
//                multipart,
//                null,
//                null,
//                converter::converterGenericError,
//                postService::postStory
//            )
//        ).thenReturn(expectedResponse)
//
//        sourceMock.postStory(postRequest, file).collect{ res ->
//            Assert.assertEquals(expectedResponse, res.data)
//        }
//
//        Mockito.verify(postService).postStory(
//            desc,
//            multipart,
//            null,
//            null
//        )
//        Mockito.verifyNoInteractions(storyDatabase)
//    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}