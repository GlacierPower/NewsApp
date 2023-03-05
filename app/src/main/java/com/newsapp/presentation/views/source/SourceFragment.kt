package com.newsapp.presentation.views.source

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
import com.newsapp.data.model.SourcesNews
import com.newsapp.databinding.FragmentSourceBinding
import com.newsapp.presentation.adapters.SourceAdapter
import com.newsapp.presentation.adapters.listener.ISourceListener
import com.newsapp.util.Resources
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SourceFragment : Fragment(), ISourceListener {

    private val viewModel: SourceViewModel by viewModels()

    private var _viewBinding: FragmentSourceBinding? = null
    private val viewBinding get() = _viewBinding!!

    private lateinit var sourceAdapter: SourceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewBinding = FragmentSourceBinding.inflate(inflater)

        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.getSourceNews()
        }

        sourceAdapter = SourceAdapter(this)
        viewBinding.sourceRecycler.apply {
            setHasFixedSize(true)
            adapter = sourceAdapter

            viewModel.source.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resources.Success -> {
                        progressBar(false)
                        tryAgain(false)
                        sourceAdapter.differ.submitList(response.data!!.sources)
                        viewBinding.sourceRecycler.adapter = sourceAdapter
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
                    viewModel.getSourceNews()
                    viewBinding.newsLayout.isRefreshing = false
                }
            }
        }
    }

    private fun progressBar(status: Boolean) {
        viewBinding.progressBar.visibility = if (status) View.VISIBLE else View.GONE

    }

    private fun tryAgain(status: Boolean) {
        if (status) {
            viewBinding.tryAgainLayout.visibility = View.VISIBLE
        } else {
            viewBinding.tryAgainLayout.visibility = View.GONE
        }
    }


    override fun onSourceClicked(sourceResponse: SourcesNews) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(sourceResponse.url))
    }


}