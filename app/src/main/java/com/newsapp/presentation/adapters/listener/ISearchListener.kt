package com.newsapp.presentation.adapters.listener

import com.newsapp.data.data_base.NewsEntity

interface ISearchListener {

    fun onShareClicked(newsResponse: NewsEntity)

    fun onItemClicked(newsResponse: NewsEntity)

    fun onFavClicked(title:String)
}