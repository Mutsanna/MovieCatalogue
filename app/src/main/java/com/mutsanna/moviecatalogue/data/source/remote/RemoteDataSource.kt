package com.mutsanna.moviecatalogue.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mutsanna.moviecatalogue.BuildConfig
import com.mutsanna.moviecatalogue.data.source.remote.api.ApiConfig
import com.mutsanna.moviecatalogue.data.source.remote.response.*
import com.mutsanna.moviecatalogue.utils.EspressoIdlingResources
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(private val apiConfig: ApiConfig){

    fun getListMovies(): LiveData<ApiResponse<List<ResultsItemMovie>>> {
        val client = apiConfig.getApiService().getListMovies(BuildConfig.MOVIE_API_KEY)
        val result = MutableLiveData<ApiResponse<List<ResultsItemMovie>>>()
        EspressoIdlingResources.increment()
        client.enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                if (response.isSuccessful) {
                    result.value = ApiResponse.success(response.body()?.results!!)
                    EspressoIdlingResources.decrement()
                }
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return result
    }

    fun getListTvShows(): LiveData<ApiResponse<List<ResultsItemTvShow>>> {
        val client = apiConfig.getApiService().getListTvShows(BuildConfig.MOVIE_API_KEY)
        val result = MutableLiveData<ApiResponse<List<ResultsItemTvShow>>>()
        EspressoIdlingResources.increment()
        client.enqueue(object : Callback<TvShowResponse> {
            override fun onResponse(call: Call<TvShowResponse>, response: Response<TvShowResponse>) {
                if (response.isSuccessful) {
                    result.value = ApiResponse.success(response.body()?.results!!)
                    EspressoIdlingResources.decrement()
                }
            }

            override fun onFailure(call: Call<TvShowResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return result
    }

    fun getDetailMovie(movieId: Int): LiveData<ApiResponse<MovieDetailResponse>> {
        val client = apiConfig.getApiService().getMovieDetail(movieId, BuildConfig.MOVIE_API_KEY)
        val result = MutableLiveData<ApiResponse<MovieDetailResponse>>()
        EspressoIdlingResources.increment()
        client.enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(call: Call<MovieDetailResponse>, response: Response<MovieDetailResponse>) {
                result.value = ApiResponse.success(response.body() as MovieDetailResponse)
                EspressoIdlingResources.decrement()
            }

            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return result
    }

    fun getDetailTvShow(showId: Int): LiveData<ApiResponse<TvShowDetailResponse>> {
        val client = apiConfig.getApiService().getTvShowDetail(showId, BuildConfig.MOVIE_API_KEY)
        val result = MutableLiveData<ApiResponse<TvShowDetailResponse>>()
        EspressoIdlingResources.increment()
        client.enqueue(object : Callback<TvShowDetailResponse> {
            override fun onResponse(call: Call<TvShowDetailResponse>, response: Response<TvShowDetailResponse>) {
                result.value = ApiResponse.success(response.body() as TvShowDetailResponse)
                EspressoIdlingResources.decrement()
            }

            override fun onFailure(call: Call<TvShowDetailResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return result
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiConfig: ApiConfig): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiConfig).apply { instance = this }
            }
    }
}