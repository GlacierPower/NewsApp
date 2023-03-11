package com.newsapp.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.newsapp.R
import com.newsapp.util.NavHelper.replaceGraph

object FragmentUtils {

   fun Fragment.refreshFragment(){
       val fragmentId = findNavController().currentDestination?.id
       findNavController().popBackStack(fragmentId!!,true)
       findNavController().navigate(fragmentId)
   }

}