package com.newsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.newsapp.databinding.FragmentSourceBinding
import com.newsapp.presentation.MainActivity
import com.newsapp.presentation.adapters.SourceAdapter
import com.newsapp.presentation.view_models.NewsViewModel
import com.newsapp.util.Constants.NAME
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SourceFragment  : DefaultFragment<FragmentSourceBinding, NewsViewModel>() {

    override val viewModel: NewsViewModel by viewModels()
    private lateinit var sourceAdapter: SourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.getSourceNews()
        }

        sourceAdapter = SourceAdapter(requireContext())
        binding.sourceRecycler.apply {
            setHasFixedSize(true)
            adapter = sourceAdapter

            viewModel.source.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resources.Success -> {
                        progressBar(false)
                        sourceAdapter.differ.submitList(response.data!!.sources)
                        binding.sourceRecycler.adapter = sourceAdapter
                    }
                    is Resources.Error -> {
                        progressBar(false)
                    }
                    is Resources.Loading -> {
                        progressBar(true)
                    }
                }
            })
        }
    }

    private fun progressBar(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSourceBinding.inflate(inflater, container, false)


}