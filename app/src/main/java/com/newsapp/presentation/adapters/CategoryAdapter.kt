package com.newsapp.presentation.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.R

class CategoryAdapter (private val categories: List<String>) :
    RecyclerView.Adapter<CategoryViewHolder>() {

    private var selectedPosition: Int = 0

    private var onItemClickListener: ((String) -> Unit)? = null

    fun onItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.items_category, parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = categories[position]

        holder.itemView.apply {
            val category = findViewById<TextView>(R.id.categoryName)
            category.text = "#$currentCategory"

            if (selectedPosition == position) {
                category.setTextColor(Color.parseColor(context.getString(R.string.category_first)))
            } else {

                category.setTextColor(Color.parseColor(context.getString(R.string.category_second)))
            }

            setOnClickListener {
                onItemClickListener?.let { it(currentCategory) }

                if (selectedPosition >= 0) {
                    notifyItemChanged(selectedPosition)
                }
                selectedPosition = holder.adapterPosition
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun getItemCount(): Int = categories.size
}

class CategoryViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem)