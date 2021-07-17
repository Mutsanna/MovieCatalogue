package com.mutsanna.moviecatalogue.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mutsanna.moviecatalogue.data.FilmRepository
import com.mutsanna.moviecatalogue.ui.detail.DetailViewModel
import com.mutsanna.moviecatalogue.di.Injection
import com.mutsanna.moviecatalogue.ui.movie.MovieViewModel
import com.mutsanna.moviecatalogue.ui.show.ShowViewModel

class ViewModelFactory private constructor(private val filmRepository: FilmRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                return MovieViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(ShowViewModel::class.java) -> {
                return ShowViewModel(filmRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(filmRepository) as T
            }
            else -> throw Throwable("Unknown View Model Class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context)).apply { instance = this }
            }
    }
}