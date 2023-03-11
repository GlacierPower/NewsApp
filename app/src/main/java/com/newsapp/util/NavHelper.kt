package com.newsapp.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

object NavHelper {

    fun Fragment.navigate(destination: Int) {
        findNavController().navigate(destination)
    }

    fun Fragment.replaceGraph(graphID: Int) {
        findNavController().setGraph(graphID)
    }

    fun Fragment.showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

}