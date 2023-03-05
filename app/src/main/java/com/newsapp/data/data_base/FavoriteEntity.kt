package com.newsapp.data.data_base

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favoriteEntity")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    val source: Source?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
): Serializable