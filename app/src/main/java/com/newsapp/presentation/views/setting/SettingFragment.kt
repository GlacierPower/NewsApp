package com.newsapp.presentation.views.setting

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.newsapp.R
import com.newsapp.databinding.FragmentSettingBinding
import com.newsapp.util.FragmentUtils.refreshFragment
import com.newsapp.util.NavHelper.navigate
import com.newsapp.util.NavHelper.replaceGraph
import com.newsapp.util.NavHelper.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels()

    private var _viewBinding: FragmentSettingBinding? = null
    private val viewBinding get() = _viewBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSettingBinding.inflate(inflater)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.theme.catch {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
            }.collect { flowBool ->
                flowBool.collect { bool ->
                    viewBinding.darkMode.isChecked != bool
                }
            }
        }

        val currentMode = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        viewBinding.darkMode.isChecked = currentMode == Configuration.UI_MODE_NIGHT_YES
        viewBinding.darkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewModel.saveTheme(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewModel.saveTheme(false)
            }
        }

        viewModel.navigateToLogin()

        viewModel.connect()

        viewBinding.login.setOnClickListener {
            viewModel.connect.observe(viewLifecycleOwner, Observer { it ->
                it.let {
                    if (it) {
                        viewModel.navLogin.observe(viewLifecycleOwner) { graph ->
                            if (graph != null) {
                                replaceGraph(graph)
                            }
                        }
                    } else showToast(getString(R.string.no_internet_connection))
                }

            })
        }

        viewModel.navigateToFavorite()
        viewBinding.favorite.setOnClickListener {
            viewModel.navFav.observe(viewLifecycleOwner) {
                if (it != null) {
                    navigate(it)
                }
            }
        }

        viewModel.navigateToSearch()
        viewBinding.search.setOnClickListener {
            viewModel.navSearch.observe(viewLifecycleOwner) {
                if (it != null) {
                    navigate(it)
                    viewModel.userNavigated()
                }
            }

        }
        viewModel.isUserLoggedIn()
        viewModel.auth.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it) {
                    viewBinding.login.visibility = View.VISIBLE
                    viewBinding.logout.visibility = View.GONE
                    viewBinding.user.setImageResource(R.drawable.profile)
                } else {
                    viewBinding.login.visibility = View.GONE
                    viewBinding.logout.visibility = View.VISIBLE
                    viewBinding.user.setBackgroundResource(R.drawable.user_ic)
                }
            }
        })

        viewBinding.logout.setOnClickListener {
            viewModel.signOut()
            refreshFragment()
        }


    }

}


