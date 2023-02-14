package com.newsapp.data.data_base

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("news_entity")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var author: String?,
    var content: String?,
    var description: String?,
    var publishedAt: String?,
    var sourceId: String?,
    var sourceName: String?,
    var title: String?,
    var url: String?,
    var urlToImage: String?,
    var page: Int?
)
