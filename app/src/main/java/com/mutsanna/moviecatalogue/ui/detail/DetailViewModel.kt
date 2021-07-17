package com.mutsanna.moviecatalogue.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mutsanna.moviecatalogue.data.FilmRepository

class DetailViewModel(private val filmRepository: FilmRepository) : ViewModel() {
    val movieId = MutableLiveData<Int>()
    val showId = MutableLiveData<Int>()

    fun setSelectedMovie(movieId: Int) {
        this.movieId.value = movieId
    }

    fun setSelectedTvShow(showId: Int) {
        this.showId.value = showId
    }

    var showDetail = Transformations.switchMap(showId) { mShowId ->
        filmRepository.getDetailTvShow(mShowId)
    }

    var movieDetail = Transformations.switchMap(movieId) { mMovieId ->
        filmRepository.getDetailMovie(mMovieId)
    }

    fun setFavorite() {
        val movie = movieDetail.value
        val show = showDetail.value

        if (movie != null) {
            val movieDetail = movie.data
            if (movieDetail != null) {
                val movieState = movieDetail.isFavorite
                val newState = !movieState
                filmRepository.setFavoriteMovie(movieDetail, newState)
            }
        }else if (show != null) {
            val showDetail = show.data
            if (showDetail != null) {
                val showState = showDetail.isFavorite
                val newState = !showState
                filmRepository.setFavoriteTvShow(showDetail, newState)
            }
        }
    }
}