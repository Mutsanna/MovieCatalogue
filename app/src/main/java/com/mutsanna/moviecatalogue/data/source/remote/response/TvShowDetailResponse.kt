package com.mutsanna.moviecatalogue.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TvShowDetailResponse(

    @field:SerializedName("backdrop_path")
    val backdropPath: String,

    @field:SerializedName("first_air_date")
    val firstAirDate: String,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("genres")
    val genres: List<GenresItem>,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("created_by")
    val createdBy: List<CreatedByItem>,

    @field:SerializedName("poster_path")
    val posterPath: String
)

data class CreatedByItem(

    @field:SerializedName("gender")
    val gender: Int,

    @field:SerializedName("credit_id")
    val creditId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("profile_path")
    val profilePath: String,

    @field:SerializedName("id")
    val id: Int
)
