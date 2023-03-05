package com.newsapp.presentation.adapters.listener

import com.newsapp.data.data_base.FavoriteEntity
import com.newsapp.data.data_base.NewsEntity

interface ISaveListener {
    fun onShareClicked(newsResponse: FavoriteEntity)

    fun onItemClicked(newsResponse: FavoriteEntity)

    fun deleteNewsByTitle(title:String)
}