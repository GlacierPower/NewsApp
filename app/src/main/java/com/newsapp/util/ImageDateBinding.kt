package com.newsapp.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.newsapp.R
import com.newsapp.di.GlideApp
import javax.inject.Inject

class ImageDateBinding @Inject constructor() {
    companion object {
        @BindingAdapter("urlToImage")
        @JvmStatic
        fun loadImage(view: ImageView, urlToImage: String?) {
            try {
                GlideApp.with(view.context).setDefaultRequestOptions(RequestOptions())
                    .load(urlToImage).placeholder(R.drawable.background_load)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
                    .error(R.drawable.background_load)
                    .into(view)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @SuppressLint("SimpleDateFormat")
        @BindingAdapter("timeAgoFormat")
        @JvmStatic
        fun convertToTimeAgoFormat(textView: TextView, time: String) {
            try {
                val timeAgo = formatTimeAgo(time)
                textView.text = timeAgo
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}