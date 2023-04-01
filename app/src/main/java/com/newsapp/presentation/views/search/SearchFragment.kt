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
import androidx.lifecycle.lifecycleScope
import com.newsapp.R
import com.newsapp.data.data_base.NewsEntity
import com.newsapp.databinding.FragmentSearchBinding
import com.newsapp.presentation.adapters.SearchAdapter
import com.newsapp.presentation.adapters.listener.ISearchListener
import com.newsapp.util.AlertListener
import com.newsapp.util.Constants.DELAY
import com.newsapp.util.Constants.ERROR
import com.newsapp.util.Constants.FAVORITE
import com.newsapp.util.Constants.PAGE_SIZE
import com.newsapp.util.FragmentUtils.showAlert
import com.newsapp.util.NavHelper.replaceGraph
import com.newsapp.util.NavHelper.showToast
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), ISearchListener, AlertListener {

    private val viewModel: SearchViewModel by viewModels()

    private var _viewBinding: FragmentSearchBinding? = null
    private val viewBinding get() = _viewBinding!!

    lateinit var searchAdapter: SearchAdapter
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

        searchAdapter = SearchAdapter(this)
        viewBinding.searchRecycler.apply {
            adapter = searchAdapter
        }

        viewModel.isUserLoggedIn()
        viewModel.navigateToLogin()

        viewModel.progressBar.observe(viewLifecycleOwner, Observer { progressBar ->
            viewBinding.loadingLayout.visibility = progressBar
        })

        viewBinding.search.addTextChangedListener { edit ->
            job?.cancel()
            job = viewLifecycleOwner.lifecycleScope.launch {
                delay(DELAY)
                edit?.let {
                    if (edit.toString().isNotEmpty() && edit.toString().length > 3) {
                        viewModel.getSearchNews(edit.toString())
                        viewModel.insertSearchNews(edit.toString())
                    }
                }
            }
        }
        viewModel.newsLD.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resources.Success -> {
                    viewModel.hideProgressBar()
                    response.data.let { searchResponse ->
                        searchAdapter.differ.submitList(searchResponse?.articles?.toList())
                        val totalResult = searchResponse!!.totalResults / PAGE_SIZE + 2
                        lastPage = viewModel.searchPage == totalResult
                        if (lastPage) {
                            viewBinding.searchRecycler.setPadding(0, 0, 0, 0)
                        }

                    }
                }
                is Resources.Error -> {
                    viewModel.hideProgressBar()
                    response.message?.let { msg ->
                        Log.e("Search fragment", "$msg")
                        Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading -> {
                    viewModel.showProgressBar()
                }
            }

        })
    }


    var lastPage = false

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
        viewModel.login.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it) {
                    viewModel.onFavClicked(title)
                    showToast(FAVORITE)
                } else
                    showAlert(this)
            }
        })
    }

    override fun showAlertDialog() {
        viewModel.loginNav.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                replaceGraph(it)
            }
        })
    }

}