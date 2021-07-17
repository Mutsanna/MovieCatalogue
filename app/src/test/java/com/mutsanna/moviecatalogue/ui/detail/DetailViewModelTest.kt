package com.mutsanna.moviecatalogue.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mutsanna.moviecatalogue.data.FilmRepository
import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import com.nhaarman.mockitokotlin2.*
import com.mutsanna.moviecatalogue.utils.ResponseFileReader
import com.mutsanna.moviecatalogue.vo.Resource
import id.ac.mymoviecatalogue.utils.DataDummy
import org.mockito.Mockito

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    private lateinit var viewModel: DetailViewModel
    private lateinit var mockWebServer: MockWebServer
    private val movieId = 460465
    private val showId = 60735

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var filmRepository: FilmRepository

    @Mock
    private lateinit var movieEntity: MoviesEntity

    @Mock
    private lateinit var showEntity: TvShowEntity

    @Mock
    private lateinit var movieObserver: Observer<Resource<MoviesEntity>>

    @Mock
    private lateinit var showObserver: Observer<Resource<TvShowEntity>>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = DetailViewModel(filmRepository)
        viewModel.setSelectedMovie(movieId)
        viewModel.setSelectedTvShow(showId)

        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun readMovieDetailJson() {
        val reader = ResponseFileReader("json/MovieDetailResponse.json")
        assertNotNull(reader.content)
    }

    @Test
    fun getMovieDetail() {
        val dummyMovie = Resource.success(movieEntity)
        val movie = MutableLiveData<Resource<MoviesEntity>>()
        movie.value = dummyMovie

        `when`(filmRepository.getDetailMovie(movieId)).thenReturn(movie)

        viewModel.movieDetail.observeForever(movieObserver)
        verify(movieObserver).onChanged(dummyMovie)
    }

    @Test
    fun readTvShowDetailJson() {
        val reader = ResponseFileReader("json/TvShowDetailResponse.json")
        assertNotNull(reader.content)
    }

    @Test
    fun getShowDetail() {
        val dummyShows = Resource.success(showEntity)
        val show = MutableLiveData<Resource<TvShowEntity>>()
        show.value = dummyShows

        `when`(filmRepository.getDetailTvShow(showId)).thenReturn(show)

        viewModel.showDetail.observeForever(showObserver)
        verify(showObserver).onChanged(dummyShows)
    }

    @Test
    fun setFavoriteMovie() {
        val movieEntity = DataDummy.generateDummyMovie()

        filmRepository.setFavoriteMovie(movieEntity, true)
        viewModel.setFavorite()
        verify(filmRepository, Mockito.times(1)).setFavoriteMovie(movieEntity, true)
    }

    @Test
    fun deleteFavoriteMovie() {
        val movieEntity = DataDummy.generateDummyMovie()

        filmRepository.setFavoriteMovie(movieEntity, false)
        viewModel.setFavorite()
        verify(filmRepository, Mockito.times(1)).setFavoriteMovie(movieEntity, false)
    }

    @Test
    fun setFavoriteTvShow() {
        val showEntity = DataDummy.generateDummyTvShow()

        filmRepository.setFavoriteTvShow(showEntity, true)
        viewModel.setFavorite()
        verify(filmRepository, Mockito.times(1)).setFavoriteTvShow(showEntity, true)
    }

    @Test
    fun deleteFavoriteTvShow() {
        val showEntity = DataDummy.generateDummyTvShow()

        filmRepository.setFavoriteTvShow(showEntity, false)
        viewModel.setFavorite()
        verify(filmRepository, Mockito.times(1)).setFavoriteTvShow(showEntity, false)
    }
}