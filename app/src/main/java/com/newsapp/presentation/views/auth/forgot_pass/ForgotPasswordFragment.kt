package com.newsapp.presentation.views.auth.forgot_pass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.newsapp.R
import com.newsapp.databinding.FragmentForgotPasswordBinding
import com.newsapp.util.NavHelper.navigate
import com.newsapp.util.NavHelper.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    private var _viewBinding: FragmentForgotPasswordBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentForgotPasswordBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.forgotPass.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.showProgressBar()
                viewModel.nav.observe(viewLifecycleOwner) { destination ->
                    if (destination != null) {
                        navigate(destination)
                    }
                }
            } else showWarning()
        })

        viewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.navigate()
                showToast(getString(R.string.check_email))
            }
        }

        viewModel.exceptionMessage.observe(viewLifecycleOwner) { msg ->
            if (msg.isNotEmpty()) {
                showToast(msg)
            }
        }
        viewBinding.btnForgot.setOnClickListener {
            val email = viewBinding.emailForgot.text.toString()
            viewModel.resetPassword(email)
        }
    }

    private fun showWarning() {
        if (!viewModel.validation(viewBinding.emailForgot.text.toString())) {
            viewBinding.layoutEmailForgot.error = getString(R.string.invalid_email)
        }
    }

}