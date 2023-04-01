package com.newsapp.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.newsapp.R
import com.newsapp.util.FragmentUtils.showAlert

object FragmentUtils {

   fun Fragment.refreshFragment(){
       val fragmentId = findNavController().currentDestination?.id
       findNavController().popBackStack(fragmentId!!,true)
       findNavController().navigate(fragmentId)
   }

fun Fragment.showAlert(alertListener: AlertListener){
    MaterialAlertDialogBuilder(requireContext())
        .setIcon(R.drawable.bookmarks)
        .setTitle(getString(R.string.add_to_favorite))
        .setMessage(getString(R.string.favorite_alert_message))
        .setPositiveButton(getString(R.string.login)) { _, _ ->
            alertListener.showAlertDialog()
        }
        .setNegativeButton(getString(R.string.cancel)) { _, _ ->
        }
        .show()


}
}