package com.newsapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.newsapp.R
import com.newsapp.databinding.ActivityMainBinding
import com.newsapp.presentation.views.ManActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI
import kotlinx.coroutines.flow.first


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: ManActivityViewModel by viewModels()

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
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        ExpandableBottomBarNavigationUI.setupWithNavController(binding.bottomBar, navController)


    }

}





