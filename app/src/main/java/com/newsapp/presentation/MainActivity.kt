package com.newsapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.newsapp.R
import com.newsapp.data.sharedpreferences.UIMode
import com.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var navController: NavController

    private lateinit var switch: SwitchMaterial

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        switch =
            binding.mainNavigationView.menu.findItem(R.id.darkModeNavDraw).actionView as SwitchMaterial

        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.newsFragment,
            R.id.categoryFragment,
            R.id.sourceFragment,
            R.id.settingFragment,
            R.id.saveFragment,
            R.id.searchFragment,
            R.navigation.auth_graph
        )
            .setOpenableLayout(binding.drawerLayout)
            .build()


        viewModel.nav.observe(this, Observer { bottomNavVisibility ->
            binding.bottomBar.visibility = bottomNavVisibility
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newsFragment, R.id.categoryFragment,
                R.id.sourceFragment, R.id.settingFragment, R.id.saveFragment,
                R.id.searchFragment -> viewModel.showBottomNav()
                else -> viewModel.hideBottomNav()
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.mainNavigationView.setupWithNavController(navController)
        ExpandableBottomBarNavigationUI.setupWithNavController(binding.bottomBar, navController)


        initView()

        viewModel.theme.asLiveData().observe(this) { uiMode ->
            setCheckedMode(uiMode)

            viewModel.navigateToLogin()

            viewModel.connect()


            navigateToAuthGraph()

            signOut()

            viewModel.isUserLoggedIn()
            viewModel.auth.observe(this, Observer {
                it.let {
                    if (it) {
                        binding.mainNavigationView.menu.findItem(R.id.btnLoginDrawer).isVisible =
                            true
                        binding.mainNavigationView.menu.findItem(R.id.btnLogOutDrawer).isVisible =
                            false
                    } else {
                        binding.mainNavigationView.menu.findItem(R.id.btnLoginDrawer).isVisible =
                            false
                        binding.mainNavigationView.menu.findItem(R.id.btnLogOutDrawer).isVisible =
                            true
                    }
                }
            })
        }

    }

    private fun signOut() {
        val btnSignOut =
            binding.mainNavigationView.menu.findItem(R.id.btnLogOutDrawer).actionView as CardView
        btnSignOut.setOnClickListener {
            viewModel.signOut()
            this.recreate()
        }
    }

    private fun navigateToAuthGraph() {
        val btnLogin =
            binding.mainNavigationView.menu.findItem(R.id.btnLoginDrawer).actionView as CardView
        btnLogin.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            viewModel.connect.observe(this, Observer { it ->
                it.let {
                    if (it) {
                        viewModel.navLogin.observe(this) { graph ->
                            if (graph != null) {
                                navController.setGraph(graph)

                            }
                        }
                    } else Toast.makeText(
                        this,
                        getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

        }
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun setCheckedMode(uiMode: UIMode?) {
        when (uiMode) {
            UIMode.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switch.isChecked = false
            }
            UIMode.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switch.isChecked = true
            }
            else -> {}
        }
    }

    private fun initView() {
        switch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> viewModel.setMode(UIMode.DARK)
                false -> viewModel.setMode(UIMode.LIGHT)
            }
        }

    }
}





