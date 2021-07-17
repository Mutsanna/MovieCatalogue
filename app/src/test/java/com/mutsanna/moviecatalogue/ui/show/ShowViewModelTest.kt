package com.mutsanna.moviecatalogue.ui.show

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.mutsanna.moviecatalogue.BuildConfig
import com.mutsanna.moviecatalogue.data.FilmRepository
import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import com.mutsanna.moviecatalogue.data.source.remote.api.ApiConfig
import com.mutsanna.moviecatalogue.data.source.remote.response.ResultsItemTvShow
import com.mutsanna.moviecatalogue.utils.ResponseFileReader
import com.mutsanna.moviecatalogue.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class ShowViewModelTest {
    private lateinit var viewModel: ShowViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    @Mock
    private lateinit var observer: Observer<Resource<PagedList<TvShowEntity>>>

    @Mock
    private lateinit var favObserver: Observer<PagedList<TvShowEntity>>

    @Mock
    private lateinit var pagedList: PagedList<TvShowEntity>

    @Mock
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = ShowViewModel(filmRepository)

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun readJson() {
        val reader = ResponseFileReader("json/TvShowsResponses.json")
        assertNotNull(reader.content)
    }

    @Test
    fun getShows() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(ResponseFileReader("json/TvShowsResponses.json").content)
        mockWebServer.enqueue(response)
        val mockRespose = response.getBody()?.readUtf8()

        val dummyShows = Resource.success(pagedList)
        `when`(dummyShows.data?.size).thenReturn(20)
        val shows = MutableLiveData<Resource<PagedList<TvShowEntity>>>()
        shows.value = dummyShows

        `when`(filmRepository.getListTvShow()).thenReturn(shows)
        val showEntity = viewModel.getShows().value?.data
        verify<FilmRepository>(filmRepository).getListTvShow()

        assertNotNull(showEntity)
        assertEquals(mockRespose?.let { getMockedJsonSize(it) }, showEntity?.size)

        viewModel.getShows().observeForever(observer)
        verify(observer).onChanged(dummyShows)
    }

    @Test
    fun getFavoriteTvShows() {
        val dummyShows = pagedList
        val shows = MutableLiveData<PagedList<TvShowEntity>>()
        shows.value = dummyShows

        `when`(filmRepository.getFavoriteTvShow()).thenReturn(shows)
        val favShows = viewModel.getFavoriteTvShows().value
        verify(filmRepository).getFavoriteTvShow()

        assertNotNull(favShows)
        assertEquals(dummyShows, favShows)

        viewModel.getFavoriteTvShows().observeForever(favObserver)
        verify(favObserver).onChanged(dummyShows)

    }

    private fun getMockedJsonSize(mockResponse: String): Int {
        val reader = JSONObject(mockResponse)
        return reader.getJSONArray("results").length()
    }
}


