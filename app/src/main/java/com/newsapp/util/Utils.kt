package com.newsapp.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.internal.Contexts.getApplication
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class InternetConnection @Inject constructor(
    private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}


fun formatTimeAgo(date1: String): String {
    var conversionTime = ""
    try {
        val format = "yyyy-MM-dd'T'HH:mm:ss'Z'"

        val sdf = SimpleDateFormat(format)

        val datetime = Calendar.getInstance()
        var date2 = sdf.format(datetime.time).toString()

        val dateObj1 = sdf.parse(date1)
        val dateObj2 = sdf.parse(date2)
        val diff = dateObj2.time - dateObj1.time

        val diffDays = diff / (24 * 60 * 60 * 1000)
        val diffhours = diff / (60 * 60 * 1000)
        val diffmin = diff / (60 * 1000)
        val diffsec = diff / 1000
        if (diffDays > 1) {
            conversionTime += "$diffDays days "
        } else if (diffhours > 1) {
            conversionTime += (diffhours - diffDays * 24).toString() + " hours "
        } else if (diffmin > 1) {
            conversionTime += (diffmin - diffhours * 60).toString() + " min "
        } else if (diffsec > 1) {
            conversionTime += (diffsec - diffmin * 60).toString() + " sec "
        }
    } catch (ex: java.lang.Exception) {
        Log.e("formatTimeAgo", ex.toString())
    }
    if (conversionTime != "") {
        conversionTime += "ago"
    }
    return conversionTime
}