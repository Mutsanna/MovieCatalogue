package com.mutsanna.moviecatalogue.data.source.local

import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import com.mutsanna.moviecatalogue.data.source.local.room.FilmDao

class LocalDataSource private constructor(private val filmDao: FilmDao){
    fun getAllMovies() = filmDao.getAllMovies()

    fun getFavoriteMovies() = filmDao.getFavoriteMovies()

    fun getMovieById(movieId: Int) = filmDao.getMovieById(movieId)

    fun getAllTvShows() = filmDao.getAllTvShows()

    fun getFavoriteTvShows() = filmDao.getFavoriteTvShow()

    fun getTvShowById(showId: Int) = filmDao.getTvShowById(showId)

    fun insertMovie(movies: List<MoviesEntity>) = filmDao.insertMovie(movies)

    fun insertTvShow(shows: List<TvShowEntity>) = filmDao.insertShow(shows)

    fun updateMovie(movie: MoviesEntity) = filmDao.updateMovie(movie)

    fun updateTvShow(show: TvShowEntity) = filmDao.updateShow(show)

    fun setFavoriteMovie(movie: MoviesEntity, newState: Boolean) {
        movie.isFavorite = newState
        filmDao.updateMovie(movie)
    }

    fun setFavoriteTvShow(show: TvShowEntity, newState: Boolean) {
        show.isFavorite = newState
        filmDao.updateShow(show)
    }

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(filmDao: FilmDao): LocalDataSource {
            return INSTANCE ?: LocalDataSource(filmDao)
        }
    }
}