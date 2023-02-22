package com.newsapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.newsapp.databinding.FragmentSearchBinding
import com.newsapp.presentation.MainActivity
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.view_models.NewsViewModel
import com.newsapp.util.Constants.DELAY
import com.newsapp.util.Constants.ERROR
import com.newsapp.util.Constants.NAME
import com.newsapp.util.Constants.PAGE_SIZE
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : DefaultFragment<FragmentSearchBinding, NewsViewModel>() {

    override val viewModel: NewsViewModel by viewModels()
    lateinit var newsAdapter: NewsAdapter
    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsAdapter(requireContext())
        binding.searchRecycler.apply {
            adapter = newsAdapter
        }
        binding.search.addTextChangedListener { edit ->
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY)
                edit?.let {
                    if (edit.toString().isNotEmpty()) {
                        viewModel.getSearchNews(edit.toString())
                    }
                }
            }
        }
        viewModel.newsLD.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data.let { searchResponse ->
                        newsAdapter.differ.submitList(searchResponse?.articles?.toList())
                        val totalResult = searchResponse!!.totalResults / PAGE_SIZE + 2
                        lastPage = viewModel.searchPage == totalResult
                        if (lastPage) {
                            binding.searchRecycler.setPadding(0, 0, 0, 0)
                        }

                    }
                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let { msg ->
                        Log.e("Search fragment", "$msg")
                        Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    loading = true
                }
            }

        })
    }

    var loading = false
    var lastPage = false

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        loading = false
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(inflater, container, false)
}