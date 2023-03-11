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

        viewModel.getSourceNews()

        sourceAdapter = SourceAdapter(this)
        viewBinding.sourceRecycler.apply {
            setHasFixedSize(true)
            adapter = sourceAdapter

            viewModel.connection.observe(viewLifecycleOwner, Observer {
                it.let {
                    if (it) {
                        viewBinding.tryAgainLayout.visibility = View.VISIBLE
                    } else {
                        viewBinding.tryAgainLayout.visibility = View.INVISIBLE
                    }
                }
            })

            viewModel.source.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resources.Success -> {
                        viewBinding.loadingLayout.visibility = View.GONE
                        sourceAdapter.differ.submitList(response.data!!.sources)
                        viewBinding.sourceRecycler.adapter = sourceAdapter
                    }
                    is Resources.Error -> {
                        viewBinding.loadingLayout.visibility = View.GONE
                        viewBinding.error.visibility = View.VISIBLE
                    }
                    is Resources.Loading -> {
                        viewBinding.loadingLayout.visibility = View.VISIBLE
                    }
                }
            })
            viewBinding.newsLayout.setOnRefreshListener {
                    viewModel.getSourceNews()
                    viewBinding.newsLayout.isRefreshing = false
            }
        }
    }


    override fun onSourceClicked(sourceResponse: SourcesNews) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(sourceResponse.url))
    }


}