package com.newsapp.data.model

import com.newsapp.data.data_base.NewsEntity

data class NewsResponse(
    var articles: MutableList<NewsEntity>,
    val status: String,
    val totalResults: Int
)
