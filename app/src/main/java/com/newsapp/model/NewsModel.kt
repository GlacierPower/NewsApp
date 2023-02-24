package com.newsapp.model

import com.newsapp.data.data_base.Source

data class NewsModel(
    var id: Int? = null,
    val source: Source,
    val author: String? = null,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
)