package com.mutsanna.moviecatalogue.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_tvShow")
data class TvShowEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "showId")
    var showId: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "releaseDate")
    var releaseDate: String,

    @ColumnInfo(name = "overview")
    var overview: String,

    @ColumnInfo(name = "creator")
    var creator: String?,

    @ColumnInfo(name = "genre")
    var genre: String?,

    @ColumnInfo(name = "poster")
    var poster: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)
