package com.newsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.newsapp.databinding.FragmentCategoryBinding
import com.newsapp.presentation.MainActivity
import com.newsapp.presentation.adapters.CategoryAdapter
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.view_models.NewsViewModel
import com.newsapp.util.Constants
import com.newsapp.util.Constants.NAME
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : DefaultFragment<FragmentCategoryBinding, NewsViewModel>() {
    override val viewModel: NewsViewModel by viewModels()
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsAdapter = NewsAdapter(requireContext())
        binding.newsRecycler.apply {
            setHasFixedSize(true)
            adapter = newsAdapter
        }

        categoryAdapter = CategoryAdapter(Constants.categories)
        categoryAdapter.onItemClickListener { news ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                viewModel.getBreakingNews(news)
            }
        }

        binding.categoriesRecycler.apply {
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
                    tryAgainStatus(true, response.message!!)
                    progressBarStatus(false)
                }
                is Resources.Loading -> {
                    tryAgainStatus(false)
                    progressBarStatus(true)
                }
            }
        })

    }


    private fun tryAgainStatus(status: Boolean, message: String = "message") {
        if (status) {
            binding.tryMessage.text = message
            binding.tryAgainLayout.visibility = View.VISIBLE
        } else {
            binding.tryAgainLayout.visibility = View.GONE
        }

    }

    private fun progressBarStatus(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCategoryBinding.inflate(inflater, container, false)
}