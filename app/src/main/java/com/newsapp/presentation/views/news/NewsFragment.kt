package com.newsapp.presentation.views.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.newsapp.R
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.data.sharedpreferences.UIMode
import com.newsapp.databinding.FragmentNewsBinding
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.adapters.listener.INewsListener
import com.newsapp.util.AlertListener
import com.newsapp.util.Constants.FAVORITE
import com.newsapp.util.FragmentUtils.showAlert
import com.newsapp.util.NavHelper.replaceGraph
import com.newsapp.util.NavHelper.showToast
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(), INewsListener, AlertListener {

    private val viewModel: NewsViewModels by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    private var _viewBinding: FragmentNewsBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewBinding = FragmentNewsBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNews()

        viewModel.progressBar.observe(viewLifecycleOwner, Observer { progressBar ->
            viewBinding.loadingLayout.visibility = progressBar
        })

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

            viewModel.newsLD.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resources.Success -> {
                        newsAdapter.differ.submitList(response.data!!.articles)
                        viewModel.hideProgressBar()
                    }
                    is Resources.Error -> {
                        viewModel.hideProgressBar()
                    }
                    is Resources.Loading -> {
                        viewModel.showProgressBar()
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
            type = getString(R.string.text_plain)
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
                    showAlert(this)
                }
            }
        })


    }

    override fun showAlertDialog() {
        viewModel.navLogin.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                replaceGraph(it)
            }
        })
    }
}