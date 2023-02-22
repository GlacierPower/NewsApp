package com.newsapp.util

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newsapp.R
import javax.inject.Inject

class ImageBinding @Inject constructor() {
    companion object {
        @BindingAdapter("urlToImage")
        @JvmStatic
        fun loadImage(view: ImageView, urlToImage: String?) {
            try {
                Glide.with(view.context).setDefaultRequestOptions(RequestOptions())
                    .load(urlToImage).placeholder(R.drawable.background_load)
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