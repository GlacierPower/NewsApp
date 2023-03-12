package com.newsapp.presentation.views.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.newsapp.R
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.sharedpreferences.UIMode
import com.newsapp.databinding.FragmentNewsBinding
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.adapters.listener.INewsListener
import com.newsapp.util.Constants.FAVORITE
import com.newsapp.util.NavHelper.replaceGraph
import com.newsapp.util.NavHelper.showToast
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(), INewsListener {

    private val viewModel: NewsViewModels by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    private var _viewBinding: FragmentNewsBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewBinding = FragmentNewsBinding.inflate(inflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNews()

        newsAdapter = NewsAdapter(this)
        viewBinding.newsRecycler.apply {
            setHasFixedSize(true)
            adapter = newsAdapter

            viewModel.isUserLoggedIn()
            viewModel.navigateToLogin()

            viewModel.connection.observe(viewLifecycleOwner, Observer {
                it.let {
                    if (it) {
                        viewBinding.tryAgainLayout.visibility = View.VISIBLE
                        viewBinding.newsRecycler.visibility = View.INVISIBLE
                    } else {
                        viewBinding.tryAgainLayout.visibility = View.INVISIBLE
                    }
                }
            })

            viewModel.theme.asLiveData().observe(viewLifecycleOwner) {
                setCheckedMode(it)
            }

            viewModel.newsLD.observe(viewLifecycleOwner, Observer { responce ->
                when (responce) {
                    is Resources.Success -> {
                        newsAdapter.differ.submitList(responce.data!!.articles)
                        viewBinding.loadingLayout.visibility = View.GONE
                    }
                    is Resources.Error -> {
                        viewBinding.tryAgainLayout.visibility = View.VISIBLE
                    }
                    is Resources.Loading -> {
                        viewBinding.loadingLayout.visibility = View.VISIBLE
                    }
                }
            })

            viewBinding.newsLayout.setOnRefreshListener {
                viewModel.getNews()
                viewBinding.newsLayout.isRefreshing = false

            }
        }

        viewModel.insertData()
    }

    override fun onShareClicked(newsResponse: NewsEntity) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, newsResponse.url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context?.startActivity(shareIntent)
    }

    override fun onItemClicked(newsResponse: NewsEntity) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(newsResponse.url))
    }

    override fun onFavClicked(title: String) {
        viewModel.userLoggedIn.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it) {
                    viewModel.onFavClicked(title)
                    showToast(FAVORITE)
                } else {
                    favoriteAlert()
                }
            }
        })


    }

    private fun favoriteAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.bookmarks)
            .setTitle(getString(R.string.add_to_favorite))
            .setMessage(getString(R.string.favorite_alert_message))
            .setPositiveButton(getString(R.string.login)) { _, _ ->
                viewModel.navLogin.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        replaceGraph(it)
                    }
                })
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            .show()


    }

    private fun setCheckedMode(uiMode: UIMode?) {
        when (uiMode) {
            UIMode.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            UIMode.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {}
        }
    }

}