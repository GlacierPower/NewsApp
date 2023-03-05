package com.newsapp.presentation.views.category

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.FragmentCategoryBinding
import com.newsapp.presentation.adapters.CategoryAdapter
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.adapters.listener.INewsListener
import com.newsapp.util.Constants
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        viewModel.newsLD.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resources.Success -> {
                    progressBarStatus(false)
                    tryAgainStatus(false)
                    newsAdapter.differ.submitList(response.data!!.articles)
                }
                is Resources.Error -> {
                    tryAgainStatus(true)
                    progressBarStatus(false)
                }
                is Resources.Loading -> {
                    tryAgainStatus(false)
                    progressBarStatus(true)
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


    private fun tryAgainStatus(status: Boolean) {
        if (status) {
            viewBinding.tryAgainLayout.visibility = View.VISIBLE
        } else {
            viewBinding.tryAgainLayout.visibility = View.GONE
        }

    }

    private fun progressBarStatus(status: Boolean) {
        viewBinding.progressBar.visibility = if (status) View.VISIBLE else View.GONE

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
        Toast.makeText(context, Constants.FAVORITE, Toast.LENGTH_LONG).show()
    }
}