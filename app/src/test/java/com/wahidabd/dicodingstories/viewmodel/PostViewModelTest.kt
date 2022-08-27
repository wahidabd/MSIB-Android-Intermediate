package com.wahidabd.dicodingstories.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.wahidabd.dicodingstories.core.Resource
import com.wahidabd.dicodingstories.core.Status
import com.wahidabd.dicodingstories.data.model.PostModel
import com.wahidabd.dicodingstories.data.request.PostRequest
import com.wahidabd.dicodingstories.data.response.GenericResponse
import com.wahidabd.dicodingstories.data.response.PostResponse
import com.wahidabd.dicodingstories.utils.CoroutinesTestRule
import com.wahidabd.dicodingstories.utils.DataDummy
import com.wahidabd.dicodingstories.utils.PagedTestDataSource
import com.wahidabd.dicodingstories.utils.getOrAwaitValue
import com.wahidabd.dicodingstories.view.page.home.PostPagingAdapter
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
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val dataDummyResponse = DataDummy.generatePostsResponse()

    @Mock
    lateinit var viewModel: PostViewModel

    @Test
    fun `Get stories - success`() = runTest {
        val dataDummy = DataDummy.generateListPost()
        val data = PagedTestDataSource.snapshot(dataDummy)

        val stories = MutableLiveData<PagingData<PostModel>>()
        stories.value = data

        `when`(viewModel.getList()).thenReturn(stories)

        val actualResult = viewModel.getList().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = PostPagingAdapter.differCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualResult)
        advanceUntilIdle()

        verify(viewModel).getList()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dataDummy.size, differ.snapshot().size)
    }

    @Test
    fun `Get stories with location - success`() = runTest {
        val expectedResponse = dataDummyResponse
        val data = MutableLiveData<Resource<PostResponse>>()
        data.value = Resource.success(expectedResponse)

        `when`(viewModel.getPosLocation()).thenReturn(data)

        val actual = viewModel.getPosLocation().getOrAwaitValue()
        verify(viewModel).getPosLocation()

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(actual.data, expectedResponse)
    }

    @Test
    fun `Get stories with location - error`() = runTest {
        val data = MutableLiveData<Resource<PostResponse>>()
        data.value = Resource.error("UNKNOWN ERROR", null)

        `when`(viewModel.getPosLocation()).thenReturn(data)

        val actual = viewModel.getPosLocation().getOrAwaitValue()
        verify(viewModel).getPosLocation()

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
    }

    @Test
    fun `Post new story - success`() = runTest {
        val expectedResponse = DataDummy.generatePostStoryResponse()
        val file = File("image")
        val request = PostRequest("desc", null, null)

        val data = MutableLiveData<Resource<GenericResponse>>()
        data.value = Resource.success(expectedResponse)

        `when`(viewModel.postStory(request, file)).thenReturn(data)
        val actual = viewModel.postStory(request, file).getOrAwaitValue()
        verify(viewModel).postStory(request, file)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertEquals(actual.data, expectedResponse)
    }

    @Test
    fun `Post new story - error`() = runTest {
        val file = File("image")
        val request = PostRequest("desc", null, null)

        val data = MutableLiveData<Resource<GenericResponse>>()
        data.value = Resource.error("UNKNOWN ERROR", null)

        `when`(viewModel.postStory(request, file)).thenReturn(data)
        val actual = viewModel.postStory(request, file).getOrAwaitValue()
        verify(viewModel).postStory(request, file)

        advanceUntilIdle()

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual.status == Status.ERROR)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}