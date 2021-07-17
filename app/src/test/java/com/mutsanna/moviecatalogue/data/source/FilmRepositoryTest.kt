package com.mutsanna.moviecatalogue.data.source

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.verify
import com.mutsanna.moviecatalogue.BuildConfig
import com.mutsanna.moviecatalogue.data.source.local.LocalDataSource
import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import com.mutsanna.moviecatalogue.data.source.local.room.FilmDao
import com.mutsanna.moviecatalogue.data.source.local.room.FilmDatabase
import com.mutsanna.moviecatalogue.data.source.remote.RemoteDataSource
import com.mutsanna.moviecatalogue.data.source.remote.api.ApiConfig
import com.mutsanna.moviecatalogue.utils.AppExecutors
import com.mutsanna.moviecatalogue.utils.PagedListUtils
import com.mutsanna.moviecatalogue.vo.Resource
import id.ac.mymoviecatalogue.utils.DataDummy
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class FilmRepositoryTest {
    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)
    private val filmRepository = FakeFilmRepository(remote, local, appExecutors)

    private lateinit var executors: AppExecutors
    private lateinit var filmDao: FilmDao
    private lateinit var db: FilmDatabase

    private val movieId = 460465
    private val showId = 60735

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, FilmDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        filmDao = db.filmDao()
        executors = AppExecutors()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getListMovies() {
        val listMovies = ApiConfig().getApiService().getListMovies(BuildConfig.MOVIE_API_KEY).execute().body()?.results

        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MoviesEntity>
        `when`(local.getAllMovies()).thenReturn(dataSourceFactory)
        filmRepository.getListMovies()

        val movies = Resource.success(PagedListUtils.mockedPagedList(listMovies!!)).data
        verify(local).getAllMovies()
        assertNotNull(movies)
        assertEquals(listMovies.size, movies?.size)
    }

    @Test
    fun getListTvShow() {
        val listShows = ApiConfig().getApiService().getListTvShows(BuildConfig.MOVIE_API_KEY).execute().body()?.results

        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        `when`(local.getAllTvShows()).thenReturn(dataSourceFactory)
        filmRepository.getListTvShow()

        val shows = Resource.success(PagedListUtils.mockedPagedList(listShows!!)).data
        verify(local).getAllTvShows()
        assertNotNull(shows)
        assertEquals(listShows.size, shows?.size)
    }

    @Test
    fun getDetailMovie() {
        val dummyDetailMovie = MutableLiveData<MoviesEntity>()
        val detailMovieEntity = filmRepository.getDetailMovie(movieId)
        dummyDetailMovie.value = detailMovieEntity.value?.data

        `when`(local.getMovieById(movieId)).thenReturn(dummyDetailMovie)

        verify(local).getMovieById(movieId)

        assertNotNull(dummyDetailMovie)
    }

    @Test
    fun getDetailTvShow() {
        val dummyDetailShow = MutableLiveData<TvShowEntity>()
        val detailShowEntity = filmRepository.getDetailTvShow(showId)
        dummyDetailShow.value = detailShowEntity.value?.data

        `when`(local.getTvShowById(showId)).thenReturn(dummyDetailShow)
        verify(local).getTvShowById(showId)

        assertNotNull(dummyDetailShow)
    }

    @Test
    fun getFavoriteMovies() {
        val listMovies = ApiConfig().getApiService().getListMovies(BuildConfig.MOVIE_API_KEY).execute().body()?.results

        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, MoviesEntity>
        `when`(local.getFavoriteMovies()).thenReturn(dataSourceFactory)
        filmRepository.getFavoriteMovies()

        val favMovies = Resource.success(PagedListUtils.mockedPagedList(listMovies!!)).data
        verify(local).getFavoriteMovies()
        assertNotNull(favMovies)
        assertEquals(listMovies.size, favMovies?.size)
    }

    @Test
    fun getFavoriteTvShows() {
        val listShows = ApiConfig().getApiService().getListTvShows(BuildConfig.MOVIE_API_KEY).execute().body()?.results

        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, TvShowEntity>
        `when`(local.getFavoriteTvShows()).thenReturn(dataSourceFactory)
        filmRepository.getFavoriteTvShow()

        val favShows = Resource.success(PagedListUtils.mockedPagedList(listShows!!)).data
        verify(local).getFavoriteTvShows()
        assertNotNull(favShows)
        assertEquals(listShows.size, favShows?.size)
    }

    @Test
    fun setFavoriteMovie() {
        val movie = DataDummy.generateDummyMovie()

        executors.diskIO().execute {
            doNothing().`when`(local).setFavoriteMovie(movie, true)
            filmRepository.setFavoriteMovie(movie, true)
            verify(local, times(1)).setFavoriteMovie(movie, true)
        }
    }

    @Test
    fun deleteFavoriteMovie() {
        val movie = DataDummy.generateDummyMovie()

        executors.diskIO().execute {
            doNothing().`when`(local).setFavoriteMovie(movie, false)
            filmRepository.setFavoriteMovie(movie, false)
            verify(local, times(1)).setFavoriteMovie(movie, false)
        }
    }

    @Test
    fun setFavoriteTvShow() {
        val show = DataDummy.generateDummyTvShow()

        executors.diskIO().execute {
            doNothing().`when`(local).setFavoriteTvShow(show, true)
            filmRepository.setFavoriteTvShow(show, true)
            verify(local, times(1)).setFavoriteTvShow(show, true)
        }
    }

    @Test
    fun deleteFavoriteTvShow() {
        val show = DataDummy.generateDummyTvShow()

        executors.diskIO().execute {
            doNothing().`when`(local).setFavoriteTvShow(show, false)
            filmRepository.setFavoriteTvShow(show, false)
            verify(local, times(1)).setFavoriteTvShow(show, false)
        }
    }
}

