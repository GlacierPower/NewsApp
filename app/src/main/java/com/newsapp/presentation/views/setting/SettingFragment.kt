package com.newsapp.presentation.views.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.newsapp.R
import com.newsapp.data.sharedpreferences.SettingDataStore
import com.newsapp.data.sharedpreferences.UIMode
import com.newsapp.databinding.FragmentSettingBinding
import com.newsapp.util.FragmentUtils.refreshFragment
import com.newsapp.util.NavHelper.navigate
import com.newsapp.util.NavHelper.replaceGraph
import com.newsapp.util.NavHelper.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        initView()

        viewModel.navigateToLogin()

        viewModel.connect()

        viewModel.theme.asLiveData().observe(viewLifecycleOwner) { uiMode ->
            setCheckedMode(uiMode)
        }

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

    private fun setCheckedMode(uiMode: UIMode?) {
        when (uiMode) {
            UIMode.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewBinding.darkMode.isChecked = false
            }
            UIMode.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewBinding.darkMode.isChecked = true
            }
            else -> {}
        }
    }

    private fun initView() {
        viewBinding.darkMode.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                when (isChecked) {
                    true -> viewModel.setMode(UIMode.DARK)
                    false -> viewModel.setMode(UIMode.LIGHT)
                }
            }
        }
    }

}


