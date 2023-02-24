package com.newsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.ItemSavedBinding
import com.newsapp.presentation.adapters.listener.INewsListener
import com.newsapp.presentation.adapters.listener.ISaveListener

class SavedAdapter(private val listener: ISaveListener) :
    RecyclerView.Adapter<SavedAdapter.SavedViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedAdapter.SavedViewHolder {
        return SavedViewHolder(
            ItemSavedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SavedAdapter.SavedViewHolder, position: Int) {
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

    inner class SavedViewHolder(
        private val itemsSavedBinding: ItemSavedBinding,
    ) :
        RecyclerView.ViewHolder(itemsSavedBinding.root) {
        fun bind(newsResponse: NewsEntity) {
            itemsSavedBinding.apply {
                itemsSavedBinding.saved = newsResponse
                itemsSavedBinding.executePendingBindings()

                itemsSavedBinding.btnShareNews.setOnClickListener {
                    listener.onShareClicked(newsResponse)
                }

                itemsSavedBinding.root.setOnClickListener {
                    listener.onItemClicked(newsResponse)
                }
                itemsSavedBinding.btnDeleteNews.setOnClickListener {
                    listener.deleteNewsByTitle(newsResponse.title)
                }
            }

        }
    }
}