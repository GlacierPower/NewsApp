package com.newsapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.newsapp.R
import com.newsapp.databinding.ActivityMainBinding
import com.newsapp.util.ThemeUtils
import dagger.hilt.android.AndroidEntryPoint
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newsFragment,
                R.id.categoryFragment,
                R.id.sourceFragment,
                R.id.settingFragment,
                R.id.saveFragment,
                R.id.searchFragment
            )
        )
        navController.addOnDestinationChangedListener { _, destinationID, _ ->
            when (destinationID.id) {
                R.id.newsFragment -> showBottomBar()
                R.id.categoryFragment -> showBottomBar()
                R.id.sourceFragment -> showBottomBar()
                R.id.settingFragment -> showBottomBar()
                R.id.saveFragment -> showBottomBar()
                R.id.searchFragment -> showBottomBar()
                else -> binding.bottomBar.visibility = View.GONE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)

        ExpandableBottomBarNavigationUI.setupWithNavController(binding.bottomBar, navController)


    }

    private fun showBottomBar() {
        binding.bottomBar.visibility = View.VISIBLE
    }


}





