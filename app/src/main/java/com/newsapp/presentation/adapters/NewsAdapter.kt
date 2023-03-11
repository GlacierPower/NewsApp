package com.newsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.ItemsNewsBinding
import com.newsapp.presentation.adapters.listener.INewsListener

class NewsAdapter(private val listener: INewsListener) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
        return NewsViewHolder(
            ItemsNewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
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

    inner class NewsViewHolder(private val itemsNewsBinding: ItemsNewsBinding) :
        RecyclerView.ViewHolder(itemsNewsBinding.root) {
        fun bind(newsEntity: NewsEntity) {
            itemsNewsBinding.apply {
                itemsNewsBinding.news = newsEntity
                itemsNewsBinding.executePendingBindings()

                itemsNewsBinding.btnShareNews.setOnClickListener {
                    listener.onShareClicked(newsEntity)
                }

                itemsNewsBinding.root.setOnClickListener {
                    listener.onItemClicked(newsEntity)
                }

                itemsNewsBinding.btnSavedNews.setOnClickListener {
                    listener.onFavClicked(newsEntity.title)
                }
            }

        }
    }
}