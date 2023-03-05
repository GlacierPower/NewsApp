package com.newsapp.presentation.views.favorite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.newsapp.R
import com.newsapp.data.data_base.FavoriteEntity
import com.newsapp.databinding.FragmentSaveBinding
import com.newsapp.presentation.adapters.CategoryAdapter
import com.newsapp.presentation.adapters.SavedAdapter
import com.newsapp.presentation.adapters.listener.ISaveListener
import com.newsapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch

@AndroidEntryPoint
class FavoriteFragment : Fragment(), ISaveListener {

    private val viewModel: FavoriteViewModel by viewModels()

    private var _viewBinding: FragmentSaveBinding? = null
    private val viewBinding get() = _viewBinding!!

    private lateinit var savedAdapter: SavedAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSaveBinding.inflate(inflater)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        savedAdapter = SavedAdapter(this)
        viewBinding.saveRecycler.apply {
            setHasFixedSize(true)
            adapter = savedAdapter
        }




        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.news.catch {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
            }.collect { flowList ->
                flowList.collect { list ->
                    savedAdapter.differ.submitList(list)
                }
            }
        }


    }
    private fun deleteAlert() =
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.delete)
            .setTitle(getString(R.string.delete_all_news))
            .setMessage(getString(R.string.are_you_sure))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    viewModel.deleteAllNews()
                }
            }
            .setNegativeButton(getString(R.string.no)) { _, _ ->
            }
            .show()


    override fun onShareClicked(newsResponse: FavoriteEntity) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, newsResponse.url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context?.startActivity(shareIntent)
    }

    override fun onItemClicked(newsResponse: FavoriteEntity) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(newsResponse.url))
    }

    override fun deleteNewsByTitle(title: String) {
        viewModel.deleteNews(title)
        Toast.makeText(context,getString(R.string.delete_from_favorite), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteAllNews -> {
                deleteAlert()
            }
        }
        return false
    }

}