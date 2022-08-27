package com.wahidabd.dicodingstories.view.page.maps

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.wahidabd.dicodingstories.R
import com.wahidabd.dicodingstories.utils.Constants.BASE_URL_MOCK
import com.wahidabd.dicodingstories.utils.JsonConverter
import com.wahidabd.dicodingstories.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@HiltAndroidTest
class MapsFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setup(){
        mockWebServer.start(8080)
        BASE_URL_MOCK = "http://127.0.0.1:808"
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun launchMapFragment_Success() {
        launchFragmentInHiltContainer<MapsFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("mock_response_success.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.maps)).check(matches(isDisplayed()))
    }
}