package com.mutsanna.moviecatalogue.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity

@Dao
interface FilmDao {
    @Query("SELECT * FROM tb_movie")
    fun getAllMovies(): DataSource.Factory<Int, MoviesEntity>

    @Query("SELECT * FROM tb_movie where isFavorite = 1")
    fun getFavoriteMovies(): DataSource.Factory<Int, MoviesEntity>

    @Query("SELECT * FROM tb_movie where movieId = :movieId AND genre IS NOT NULL AND production IS NOT NULL")
    fun getMovieById(movieId: Int): LiveData<MoviesEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movies: List<MoviesEntity>)

    @Update
    fun updateMovie(movie: MoviesEntity)

    @Query("SELECT * FROM tb_tvShow")
    fun getAllTvShows(): DataSource.Factory<Int, TvShowEntity>

    @Query("SELECT * FROM tb_tvShow where isFavorite = 1")
    fun getFavoriteTvShow(): DataSource.Factory<Int, TvShowEntity>

    @Query("SELECT * FROM tb_tvshow where showId = :showId AND genre IS NOT NULL AND creator IS NOT NULL")
    fun getTvShowById(showId: Int): LiveData<TvShowEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertShow(show: List<TvShowEntity>)

    @Update
    fun updateShow(show: TvShowEntity)
}