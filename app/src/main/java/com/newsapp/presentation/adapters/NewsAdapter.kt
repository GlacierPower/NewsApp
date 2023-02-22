package com.newsapp.presentation.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.ItemsNewsBinding

class NewsAdapter (private val context: Context) :
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
        fun bind(newsResponse: NewsEntity) {
            itemsNewsBinding.apply {
                itemsNewsBinding.news = newsResponse
                itemsNewsBinding.executePendingBindings()

                itemsNewsBinding.btnShareNews.setOnClickListener {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, newsResponse.url)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }

                itemsNewsBinding.root.setOnClickListener {
                    val builder = CustomTabsIntent.Builder()
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(context, Uri.parse(newsResponse.url))
                }


            }

        }
    }
}