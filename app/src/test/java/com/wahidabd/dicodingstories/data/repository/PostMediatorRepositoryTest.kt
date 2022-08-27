package com.wahidabd.dicodingstories.data.repository

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.wahidabd.dicodingstories.utils.CoroutinesTestRule
import com.wahidabd.dicodingstories.utils.DataDummy
import com.wahidabd.dicodingstories.utils.PagedTestDataSource
import com.wahidabd.dicodingstories.view.page.home.PostPagingAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostMediatorRepositoryTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var repo: PostMediatorRepository

    @Test
    fun `Gate paged stories - success`() = runTest {
        val dummyData = DataDummy.generateListPost()
        val data = PagedTestDataSource.snapshot(dummyData)
        val expected = flowOf(data)

        val response = DataDummy.generatePostsResponse()

        `when`(repo.getListPost()).thenReturn(expected)
        repo.getListPost().collect{actual ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = PostPagingAdapter.differCallback,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(actual)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(response.listStory.size, differ.snapshot().size)
        }
    }


    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
