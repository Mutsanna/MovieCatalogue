package com.mutsanna.moviecatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mutsanna.moviecatalogue.data.FilmDataSource
import com.mutsanna.moviecatalogue.data.NetworkBoundResource
import com.mutsanna.moviecatalogue.data.source.local.LocalDataSource
import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import com.mutsanna.moviecatalogue.data.source.remote.ApiResponse
import com.mutsanna.moviecatalogue.data.source.remote.RemoteDataSource
import com.mutsanna.moviecatalogue.data.source.remote.response.MovieDetailResponse
import com.mutsanna.moviecatalogue.data.source.remote.response.ResultsItemMovie
import com.mutsanna.moviecatalogue.data.source.remote.response.ResultsItemTvShow
import com.mutsanna.moviecatalogue.data.source.remote.response.TvShowDetailResponse
import com.mutsanna.moviecatalogue.utils.AppExecutors
import com.mutsanna.moviecatalogue.vo.Resource

class FakeFilmRepository (
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): FilmDataSource {
    override fun getListMovies(): LiveData<Resource<PagedList<MoviesEntity>>> {
        return object : NetworkBoundResource<PagedList<MoviesEntity>, List<ResultsItemMovie>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<MoviesEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(20)
                    .setPageSize(20)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllMovies(), config).build()
            }

            override fun shouldFetch(data: PagedList<MoviesEntity>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<ResultsItemMovie>>> {
                return remoteDataSource.getListMovies()
            }

            override fun saveCallResult(data: List<ResultsItemMovie>) {
                val movieList = ArrayList<MoviesEntity>()
                for (response in data) {
                    val moviesEntity = MoviesEntity(
                        response.id,
                        response.title,
                        response.releaseDate,
                        response.overview,
                        null,
                        null,
                        response.posterPath,
                        false
                    )
                    movieList.add(moviesEntity)
                }
                localDataSource.insertMovie(movieList)
            }
        }.asLiveData()
    }

    override fun getFavoriteMovies(): LiveData<PagedList<MoviesEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()
        return LivePagedListBuilder(localDataSource.getFavoriteMovies(), config).build()
    }

    override fun getListTvShow(): LiveData<Resource<PagedList<TvShowEntity>>> {
        return object : NetworkBoundResource<PagedList<TvShowEntity>, List<ResultsItemTvShow>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<TvShowEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(20)
                    .setPageSize(20)
                    .build()
                return LivePagedListBuilder(localDataSource.getAllTvShows(), config).build()
            }

            override fun shouldFetch(data: PagedList<TvShowEntity>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun createCall(): LiveData<ApiResponse<List<ResultsItemTvShow>>> {
                return remoteDataSource.getListTvShows()
            }

            override fun saveCallResult(data: List<ResultsItemTvShow>) {
                val showList = ArrayList<TvShowEntity>()
                for (response in data) {
                    val show = TvShowEntity(
                        response.id,
                        response.name,
                        response.firstAirDate,
                        response.overview,
                        null,
                        null,
                        response.posterPath,
                        false
                    )
                    showList.add(show)
                }
                localDataSource.insertTvShow(showList)
            }
        }.asLiveData()
    }

    override fun getFavoriteTvShow(): LiveData<PagedList<TvShowEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .build()
        return LivePagedListBuilder(localDataSource.getFavoriteTvShows(), config).build()
    }

    override fun getDetailMovie(movieId: Int): LiveData<Resource<MoviesEntity>> {
        return object : NetworkBoundResource<MoviesEntity, MovieDetailResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<MoviesEntity> {
                return localDataSource.getMovieById(movieId)
            }

            override fun shouldFetch(data: MoviesEntity?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<MovieDetailResponse>> {
                return remoteDataSource.getDetailMovie(movieId)
            }

            override fun saveCallResult(data: MovieDetailResponse) {
                val dataEntity = MoviesEntity(
                    data.id,
                    data.title,
                    data.releaseDate,
                    data.overview,
                    data.genres.joinToString { it.name },
                    data.productionCompanies.joinToString { it.name },
                    data.posterPath,
                    false
                )
                localDataSource.updateMovie(dataEntity)
            }
        }.asLiveData()
    }

    override fun getDetailTvShow(showId: Int): LiveData<Resource<TvShowEntity>> {
        return object : NetworkBoundResource<TvShowEntity, TvShowDetailResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<TvShowEntity> {
                return localDataSource.getTvShowById(showId)
            }

            override fun shouldFetch(data: TvShowEntity?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<TvShowDetailResponse>> {
                return remoteDataSource.getDetailTvShow(showId)
            }

            override fun saveCallResult(data: TvShowDetailResponse) {
                val show = TvShowEntity(
                    data.id,
                    data.name,
                    data.firstAirDate,
                    data.overview,
                    data.createdBy.joinToString { it.name },
                    data.genres.joinToString { it.name },
                    data.posterPath,
                    false
                )
                localDataSource.updateTvShow(show)
            }
        }.asLiveData()
    }

    override fun setFavoriteMovie(movie: MoviesEntity, state: Boolean) {
        appExecutors.diskIO().execute { localDataSource.setFavoriteMovie(movie, state) }
    }

    override fun setFavoriteTvShow(show: TvShowEntity, state: Boolean) {
        appExecutors.diskIO().execute { localDataSource.setFavoriteTvShow(show, state) }
    }
}