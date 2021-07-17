package com.mutsanna.moviecatalogue.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.google.gson.Gson
import com.mutsanna.moviecatalogue.BuildConfig
import com.mutsanna.moviecatalogue.data.FilmRepository
import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import com.mutsanna.moviecatalogue.data.source.remote.api.ApiConfig
import com.mutsanna.moviecatalogue.data.source.remote.response.ResultsItemMovie
import com.mutsanna.moviecatalogue.data.source.remote.response.TvShowResponse
import com.mutsanna.moviecatalogue.utils.JsonFilesInKt
import com.mutsanna.moviecatalogue.utils.ResponseFileReader
import com.mutsanna.moviecatalogue.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import id.ac.mymoviecatalogue.utils.DataDummy
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {
    private lateinit var viewModel: MovieViewModel

    @get:Rule
    var instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    @Mock
    private lateinit var pagedList: PagedList<MoviesEntity>

    @Mock
    private lateinit var observer: Observer<Resource<PagedList<MoviesEntity>>>

    @Mock
    private lateinit var favObserver: Observer<PagedList<MoviesEntity>>

    @Mock
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = MovieViewModel(filmRepository)

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun readJson() {
        val reader = ResponseFileReader("json/MoviesResponses.json")
        assertNotNull(reader.content)
    }

    @Test
    fun getMovies() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(ResponseFileReader("json/MoviesResponses.json").content)
        mockWebServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()

        val dummyMovies = Resource.success(pagedList)
        `when`(dummyMovies.data?.size).thenReturn(20)
        val movies = MutableLiveData<Resource<PagedList<MoviesEntity>>>()
        movies.value = dummyMovies

        `when`(filmRepository.getListMovies()).thenReturn(movies)
        val moviesEntity = viewModel.getMovies().value?.data
        verify(filmRepository).getListMovies()

        assertNotNull(moviesEntity)
        assertEquals(mockResponse?.let { getMockedJsonSize(it) }, moviesEntity?.size)

        viewModel.getMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovies)
    }

    @Test
    fun getFavoriteMovies() {
        val dummyMovie = pagedList
        val movies = MutableLiveData<PagedList<MoviesEntity>>()
        movies.value = dummyMovie

        `when`(filmRepository.getFavoriteMovies()).thenReturn(movies)
        val favMovies = viewModel.getFavoriteMovies().value
        verify(filmRepository).getFavoriteMovies()

        assertNotNull(favMovies)
        assertEquals(dummyMovie, favMovies)

        viewModel.getFavoriteMovies().observeForever(favObserver)
        verify(favObserver).onChanged(dummyMovie)
    }

    private fun getMockedJsonSize(mockResponse: String): Int {
        val reader = JSONObject(mockResponse)
        return reader.getJSONArray("results").length()
    }
}