package com.newsapp.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.newsapp.R
import com.newsapp.databinding.FragmentNewsBinding
import com.newsapp.presentation.MainActivity
import com.newsapp.presentation.adapters.NewsAdapter
import com.newsapp.presentation.view_models.NewsViewModel
import com.newsapp.util.Constants
import com.newsapp.util.Constants.NAME
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class NewsFragment : DefaultFragment<FragmentNewsBinding, NewsViewModel>() {

    override val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            val isChecked = viewModel.getTheme.first()
            setTheme(isChecked)
        }

        newsAdapter = NewsAdapter(requireContext())
        binding.newsRecycler.apply {
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
                            tryAgain(true, responce.message!!)
                            progressBar(false)
                        }
                        is Resources.Loading -> {
                            tryAgain(false)
                            progressBar(true)
                        }
                    }
                })

                binding.newsLayout.setOnRefreshListener {
                    viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                        viewModel.getNews()
                        binding.newsLayout.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun tryAgain(status: Boolean, message: String = "message") {
        if (status) {

            binding.message.text = message
            binding.tryAgainLayout.visibility = View.VISIBLE
        } else {
            binding.tryAgainLayout.visibility = View.GONE
        }
    }

    private fun progressBar(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE
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


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewsBinding.inflate(inflater, container, false)

}