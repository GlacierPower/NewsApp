package com.newsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.ItemSearchBinding
import com.newsapp.presentation.adapters.listener.ISearchListener

class SearchAdapter(private val listener: ISearchListener) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchAdapter.SearchViewHolder {
        return SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchAdapter.SearchViewHolder, position: Int) {
        val news = differ.currentList[position]
        holder.bind(news)

    }

    private val differCallback =
        object : DiffUtil.ItemCallback<NewsEntity>() {
            override fun areItemsTheSame(
                oldItem: NewsEntity,
                newItem: NewsEntity
            ): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(
                oldItem: NewsEntity,
                newItem: NewsEntity
            ): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class SearchViewHolder(private val itemSearchBinding: ItemSearchBinding) :
        RecyclerView.ViewHolder(itemSearchBinding.root) {
        fun bind(newsResponse: NewsEntity) {
            itemSearchBinding.apply {
                itemSearchBinding.news = newsResponse
                itemSearchBinding.executePendingBindings()

                itemSearchBinding.btnShareNews.setOnClickListener {
                    listener.onShareClicked(newsResponse)
                }

                itemSearchBinding.root.setOnClickListener {
                    listener.onItemClicked(newsResponse)
                }
                itemSearchBinding.btnSavedNews.setOnClickListener {
                    listener.onFavClicked(newsResponse.title)
                }

            }

        }
    }
}