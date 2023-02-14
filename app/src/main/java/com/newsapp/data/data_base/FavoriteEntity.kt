package com.newsapp.data.data_base

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favoriteEntity")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Long=0,
    @ColumnInfo(name = "author")
    var author: String?,
    @ColumnInfo(name = "content")
    var content: String?,
    @ColumnInfo(name = "description")
    var description: String?,
    @ColumnInfo(name = "publishedAt")
    var publishedAt: String?,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "url")
    var url: String?,
    @ColumnInfo(name = "urlToImage")
    var urlToImage: String?,
    @ColumnInfo(name = "category")
    var category: String? = "",
    @ColumnInfo(name = "isFavourite")
    var isFavourite: Int? = 0
)