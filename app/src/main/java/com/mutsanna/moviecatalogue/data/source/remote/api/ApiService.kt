package com.mutsanna.moviecatalogue.data.source.remote.api

import com.mutsanna.moviecatalogue.data.source.remote.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("3/tv/airing_today")
    fun getListTvShows(@Query("api_key") apiKey: String): Call<TvShowResponse>

    @GET("3/tv/{tv_id}")
    fun getTvShowDetail(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvShowDetailResponse>

    @GET("3/movie/now_playing")
    fun getListMovies(@Query("api_key") apiKey: String): Call<MoviesResponse>

    @GET("3/movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailResponse>
}