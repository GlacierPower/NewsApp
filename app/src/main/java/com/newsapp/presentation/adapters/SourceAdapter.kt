package com.newsapp.presentation.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.data.model.SourcesNews
import com.newsapp.databinding.ItemsSourceBinding
import com.newsapp.presentation.adapters.listener.ISourceListener

class SourceAdapter(private val listener:ISourceListener) :
    RecyclerView.Adapter<SourceAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SourceAdapter.NewsViewHolder {
        return NewsViewHolder(
            ItemsSourceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SourceAdapter.NewsViewHolder, position: Int) {
        val news = differ.currentList[position]
        holder.bind(news)

    }

    private val differCallback =
        object : DiffUtil.ItemCallback<SourcesNews>() {
            override fun areItemsTheSame(
                oldItem: SourcesNews,
                newItem: SourcesNews
            ): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(
                oldItem: SourcesNews,
                newItem: SourcesNews
            ): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NewsViewHolder(private val itemsSourceNewsBinding: ItemsSourceBinding) :
        RecyclerView.ViewHolder(itemsSourceNewsBinding.root) {
        fun bind(sourceResponse: SourcesNews) {
            itemsSourceNewsBinding.apply {
                itemsSourceNewsBinding.source = sourceResponse
                itemsSourceNewsBinding.executePendingBindings()

                itemsSourceNewsBinding.root.setOnClickListener {
                    listener.onSourceClicked(sourceResponse)

                }

            }

        }
    }

}