package com.mutsanna.moviecatalogue.ui.show

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mutsanna.moviecatalogue.data.FilmRepository
import com.mutsanna.moviecatalogue.data.source.local.entity.TvShowEntity
import com.mutsanna.moviecatalogue.vo.Resource

class ShowViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    fun getShows(): LiveData<Resource<PagedList<TvShowEntity>>> = filmRepository.getListTvShow()

    fun getFavoriteTvShows() = filmRepository.getFavoriteTvShow()
}