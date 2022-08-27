package com.wahidabd.dicodingstories.view.page.home

import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.utils.Constants.BASE_URL_MOCK
import com.wahidabd.dicodingstories.utils.EspressoIdlingResource
import com.wahidabd.dicodingstories.utils.JsonConverter
import com.wahidabd.dicodingstories.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@ExperimentalPagingApi
@HiltAndroidTest
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        BASE_URL_MOCK = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun launchHomeFragment_Success(){
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mock_response_success.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_posts)).check(matches(isDisplayed()))
    }

    @Test
    fun launcherHomeFragment_Empty(){
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mock_response_empty.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.home_error)).check(matches(not(isDisplayed())))
    }

    @Test
    fun launchHomeFragment_Failed(){
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody(JsonConverter.readStringFromFile("mock_response_empty.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.home_error)).check(matches(not(isDisplayed())))
    }

}