package com.mutsanna.moviecatalogue.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mutsanna.moviecatalogue.data.FilmRepository
import com.mutsanna.moviecatalogue.data.source.local.entity.MoviesEntity
import com.mutsanna.moviecatalogue.vo.Resource

class MovieViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    fun getMovies(): LiveData<Resource<PagedList<MoviesEntity>>> = filmRepository.getListMovies()

    fun getFavoriteMovies() = filmRepository.getFavoriteMovies()
}