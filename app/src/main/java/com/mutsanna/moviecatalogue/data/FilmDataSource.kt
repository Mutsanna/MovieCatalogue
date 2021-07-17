package com.mutsanna.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import com.mutsanna.moviecatalogue.vo.Resource

interface FilmDataSource {
    fun getListMovies(): LiveData<Resource<PagedList<MoviesEntity>>>

    fun getFavoriteMovies(): LiveData<PagedList<MoviesEntity>>

    fun getListTvShow(): LiveData<Resource<PagedList<TvShowEntity>>>

    fun getFavoriteTvShow(): LiveData<PagedList<TvShowEntity>>

    fun getDetailMovie(movieId: Int): LiveData<Resource<MoviesEntity>>

    fun getDetailTvShow(showId: Int): LiveData<Resource<TvShowEntity>>

    fun setFavoriteMovie(movie: MoviesEntity, state: Boolean)

    fun setFavoriteTvShow(show: TvShowEntity, state: Boolean)
}