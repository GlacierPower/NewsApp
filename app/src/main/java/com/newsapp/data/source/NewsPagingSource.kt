package com.newsapp.data.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.newsapp.data.dao.NewsDao
import com.newsapp.data.model.News
import com.newsapp.data.service.ApiService
import com.newsapp.model.NewsModel
import com.newsapp.model.asDatabaseModel
import com.newsapp.utils.Constants.ERROR_MESSAGE
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class NewsPagingSource(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val category: String,
    private val country: String
) : PagingSource<Int, News>() {

    companion object {
        private const val TAG = "NewsPagingSource"
        private const val STARTING_PAGE_NUMBER = 1
    }

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val page = params.key ?: STARTING_PAGE_NUMBER
        return try {
            val response = apiService.getTopHedLinesWS(
                country = country,
                category = category,
                page = page
            )
            if (response.isSuccessful && response.code() == 200 && response.body() != null) {
                if (response.body()!!.articles != null) {
                    val article = response.body()!!.articles!!
                    Log.d("TAG", "load: ${article.size}")
                    LoadResult.Page(
                        data = article,
                        nextKey = if (article.isNotEmpty()) page + 1 else null,
                        prevKey = if (page == STARTING_PAGE_NUMBER) null else page - 1
                    )
                } else LoadResult.Error(Throwable(ERROR_MESSAGE))
            } else LoadResult.Error(Throwable("${response.code()}"))
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private suspend fun deletePreviousNews(
        response: Response<NewsModel>,
        page: Int
    ) {
        Log.d(TAG, "deletePreviousNews: ")
        newsDao.deleteNewsFromDB(response.body()?.asDatabaseModel(page)!!)
    }

    private suspend fun insertNewsToDB(
        response: Response<NewsModel>,
        page: Int
    ) {
        Log.d(TAG, "insertNewsToDB: ")
        newsDao.insertNewsLis(response.body()?.asDatabaseModel(page)!!)
    }

}