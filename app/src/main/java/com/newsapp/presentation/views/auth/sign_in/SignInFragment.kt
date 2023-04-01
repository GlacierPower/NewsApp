package com.newsapp.presentation.views.auth.sign_in

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.newsapp.R
import com.newsapp.databinding.FragmentSignInBinding
import com.newsapp.util.NavHelper.navigate
import com.newsapp.util.NavHelper.replaceGraph
import com.newsapp.util.NavHelper.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _viewBinding: FragmentSignInBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel: SignInViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSignInBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.progressBar.observe(viewLifecycleOwner, Observer { progressBar ->
            viewBinding.loadingLayout.visibility = progressBar
        })
        viewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.replaceGraph()
            }
        }
        viewModel.exceptionMessage.observe(viewLifecycleOwner) { msg ->
            if (msg.isNotEmpty()) {
                viewModel.hideProgressBar()
                showToast(msg)
            }
        }

        viewModel.signIn.observe(viewLifecycleOwner, Observer {
            val view: View? = activity?.currentFocus
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (it) {
                viewModel.showProgressBar()
                viewModel.nav.observe(viewLifecycleOwner) { destination ->
                    if (destination != null) {
                        replaceGraph(destination)
                        activity?.recreate()
                    }
                }
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            } else showWarning()
        })

        viewBinding.btnLogin.setOnClickListener {
            signIn()
        }


        viewBinding.forgotPass.setOnClickListener {
            navigateToForgotPassword()
        }

        viewBinding.signUp.setOnClickListener {
            navigateToSignUp()
        }

    }

    private fun signIn() {
        val email = viewBinding.email.text.toString()
        val pass = viewBinding.password.text.toString()
        viewModel.signIn(email, pass)
    }

    private fun navigateToForgotPassword() {
        viewModel.navigateToForgotPassword()
        viewModel.navToForgot.observe(viewLifecycleOwner) {
            if (it != null) {
                navigate(it)
            }
        }
    }

    private fun navigateToSignUp() {
        viewModel.navigateToSignUp()
        viewModel.navToSignUp.observe(viewLifecycleOwner) {
            if (it != null) {
                navigate(it)
            }
        }
    }

    private fun showWarning() {
        if (!viewModel.validateEmail(viewBinding.email.text.toString())) {
            viewBinding.layoutEmail.error = getString(R.string.invalid_email)
        }

        if (!viewModel.validatePassword(viewBinding.password.text.toString())) {
            viewBinding.layoutPassword.error = getString(R.string.password_wrong)
        }

    }

}