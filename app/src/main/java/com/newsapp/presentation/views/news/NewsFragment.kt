package com.newsapp.presentation.views.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.FragmentNewsBinding
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.adapters.listener.INewsListener
import com.newsapp.util.Constants.FAVORITE
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

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


        lifecycleScope.launchWhenResumed {
            val isChecked = viewModel.getTheme.first()
            setTheme(isChecked)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.getNews()
        }

        newsAdapter = NewsAdapter(this)
        viewBinding.newsRecycler.apply {
            setHasFixedSize(true)
            adapter = newsAdapter

            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                viewModel.newsLD.observe(viewLifecycleOwner, Observer { responce ->
                    when (responce) {
                        is Resources.Success -> {
                            progressBar(false)
                            tryAgain(false)
                            newsAdapter.differ.submitList(responce.data!!.articles)
                        }
                        is Resources.Error -> {
                            tryAgain(true)
                            progressBar(false)
                        }
                        is Resources.Loading -> {
                            tryAgain(false)
                            progressBar(true)
                        }
                    }
                })

                viewBinding.newsLayout.setOnRefreshListener {
                    viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                        viewModel.getNews()
                        viewBinding.newsLayout.isRefreshing = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.insertData()
        }
    }

    private fun tryAgain(status: Boolean) {
        if (status) {
            viewBinding.tryAgainLayout.visibility = View.VISIBLE
        } else {
            viewBinding.tryAgainLayout.visibility = View.GONE
        }
    }

    private fun progressBar(status: Boolean) {
        viewBinding.progressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun setTheme(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            viewModel.saveTheme(true)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            viewModel.saveTheme(false)
        }
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
        viewModel.onFavClicked(title)
        Toast.makeText(context, FAVORITE, Toast.LENGTH_LONG).show()

    }

}