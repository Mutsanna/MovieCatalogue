package com.mutsanna.moviecatalogue.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MoviesResponse(

    @field:SerializedName("results")
    val results: List<ResultsItemMovie>
)

data class ResultsItemMovie(

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("genre_ids")
    val genreIds: List<Int>,

    @field:SerializedName("poster_path")
    val posterPath: String,

    @field:SerializedName("backdrop_path")
    val backdropPath: String,

    @field:SerializedName("release_date")
    val releaseDate: String,

    @field:SerializedName("id")
    val id: Int
)
