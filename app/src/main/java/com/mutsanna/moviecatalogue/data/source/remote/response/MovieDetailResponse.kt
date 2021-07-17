package com.mutsanna.moviecatalogue.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(

    @field:SerializedName("backdrop_path")
    val backdropPath: String,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("release_date")
    val releaseDate: String,

    @field:SerializedName("genres")
    val genres: List<GenresItem>,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("poster_path")
    val posterPath: String,

    @field:SerializedName("production_companies")
    val productionCompanies: List<ProductionCompaniesItem>
)

data class ProductionCompaniesItem(

    @field:SerializedName("logo_path")
    val logoPath: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("origin_country")
    val originCountry: String
)
