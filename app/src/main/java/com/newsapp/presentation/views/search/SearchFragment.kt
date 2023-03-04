package com.newsapp.presentation.views.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.FragmentSearchBinding
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.adapters.listener.INewsListener
import com.newsapp.util.Constants.DELAY
import com.newsapp.util.Constants.ERROR
import com.newsapp.util.Constants.PAGE_SIZE
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), INewsListener {

    private val viewModel: SearchViewModel by viewModels()

    private var _viewBinding: FragmentSearchBinding? = null
    private val viewBinding get() = _viewBinding!!

    lateinit var newsAdapter: NewsAdapter
    var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewBinding = FragmentSearchBinding.inflate(inflater)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsAdapter(this)
        viewBinding.searchRecycler.apply {
            adapter = newsAdapter
        }
        viewBinding.search.addTextChangedListener { edit ->
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY)
                edit?.let {
                    if (edit.toString().isNotEmpty() && edit.toString().length > 3) {
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
                            viewBinding.searchRecycler.setPadding(0, 0, 0, 0)
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
                    viewBinding.progressBar.visibility = View.VISIBLE
                    loading = true
                }
            }

        })
    }

    var loading = false
    var lastPage = false

    private fun hideProgressBar() {
        viewBinding.progressBar.visibility = View.INVISIBLE
        loading = false
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
}