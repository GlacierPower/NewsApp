package com.newsapp.presentation.adapters.listener

import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.model.SourcesNews

interface ISourceListener {
    fun onSourceClicked(sourceResponse: SourcesNews)
}