package com.mutsanna.moviecatalogue.di

import android.content.Context
import com.mutsanna.moviecatalogue.data.FilmRepository
import com.mutsanna.moviecatalogue.data.source.local.LocalDataSource
import com.mutsanna.moviecatalogue.data.source.local.room.FilmDatabase
import com.mutsanna.moviecatalogue.data.source.remote.RemoteDataSource
import com.mutsanna.moviecatalogue.data.source.remote.api.ApiConfig
import com.mutsanna.moviecatalogue.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FilmRepository {
        val db = FilmDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig())
        val localDataSource = LocalDataSource.getInstance(db.filmDao())
        val appExecutors = AppExecutors()

        return FilmRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}
