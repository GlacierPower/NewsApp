package com.newsapp.presentation.views.category

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.newsapp.R
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.FragmentCategoryBinding
import com.newsapp.presentation.adapters.CategoryAdapter
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.adapters.listener.INewsListener
import com.newsapp.util.Constants
import com.newsapp.util.Constants.FAVORITE
import com.newsapp.util.NavHelper.replaceGraph
import com.newsapp.util.NavHelper.showToast
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(), INewsListener {
    private val viewModel: CategoryViewModel by viewModels()
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var newsAdapter: NewsAdapter

    private var _viewBinding: FragmentCategoryBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewBinding = FragmentCategoryBinding.inflate(inflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = NewsAdapter(this)
        viewBinding.newsRecycler.apply {
            setHasFixedSize(true)
            adapter = newsAdapter
        }

        categoryAdapter = CategoryAdapter(Constants.categories)
        viewBinding.categoriesRecycler.apply {
            adapter = categoryAdapter
        }

        viewModel.navigateToLogin()
        viewModel.isUserLoggedIn()

        viewModel.connection.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it) {
                    viewBinding.tryAgainLayout.visibility = View.VISIBLE
                } else {
                    viewBinding.tryAgainLayout.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.newsLD.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resources.Success -> {
                    viewBinding.loadingLayout.visibility = View.GONE
                    newsAdapter.differ.submitList(response.data!!.articles)
                }
                is Resources.Error -> {
                    viewBinding.error. visibility = View.VISIBLE
                }
                is Resources.Loading -> {
                    viewBinding.loadingLayout.visibility = View.VISIBLE
                }
            }
        })

        categoryAdapter.onItemClickListener { news ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                viewModel.getBreakingNews(news)
                viewModel.insertCategory(news)
            }
        }


    }

    private fun progressBarStatus(status: Boolean) {
        viewBinding.loadingLayout.visibility = if (status) View.VISIBLE else View.GONE

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
                viewModel.loginNav.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        replaceGraph(it)
                    }
                })
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            .show()
    }
}