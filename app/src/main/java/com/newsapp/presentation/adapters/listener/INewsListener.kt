package com.newsapp.presentation.adapters.listener

import com.newsapp.data.data_base.NewsEntity

interface INewsListener {

    fun onShareClicked(newsResponse: NewsEntity)

    fun onItemClicked(newsResponse: NewsEntity)
}