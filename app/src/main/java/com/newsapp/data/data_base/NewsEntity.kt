package com.newsapp.data.data_base

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "newsEntity",
    indices = [Index(
        value = ["title", "publishedAt"],
        unique = true
    )]
)
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var author: String,
    val content: String?,
    val description: String,
    var publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)