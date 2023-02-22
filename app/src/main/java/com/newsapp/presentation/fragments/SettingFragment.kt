package com.newsapp.presentation.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.newsapp.databinding.FragmentSettingBinding
import com.newsapp.presentation.MainActivity
import com.newsapp.presentation.view_models.NewsViewModel
import com.newsapp.util.Constants.NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : DefaultFragment<FragmentSettingBinding, NewsViewModel>() {

    override val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentMode = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        binding.switch1.isChecked = currentMode == Configuration.UI_MODE_NIGHT_YES
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                viewModel.saveTheme(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                viewModel.saveTheme(false)
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingBinding.inflate(inflater, container, false)
}