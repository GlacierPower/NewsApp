package com.newsapp.presentation.views.auth.sign_in

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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


        viewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.replaceGraph()
            }
        }
        viewModel.exceptionMessage.observe(viewLifecycleOwner) { msg ->
            if (msg.isNotEmpty()) {
                viewBinding.loadingLayout.visibility = View.GONE
                showToast(msg)
            }
        }
        viewBinding.btnLogin.setOnClickListener {
            val view: View? = activity?.currentFocus
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val email = viewBinding.email.text.toString()
            val pass = viewBinding.password.text.toString()
            if (viewModel.signIn(email, pass)) {
                viewBinding.loadingLayout.visibility = View.VISIBLE
                viewModel.nav.observe(viewLifecycleOwner) {
                    if (it != null) {
                        replaceGraph(it)
                    }

                }
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
            } else showWarning()
        }

        viewModel.navigateToForgotPassword()
        viewBinding.forgotPass.setOnClickListener {
            viewModel.navToForgot.observe(viewLifecycleOwner) {
                if (it != null) {
                    navigate(it)
                }
            }

        }
        viewModel.navigateToSignUp()
        viewBinding.signUp.setOnClickListener {
            viewModel.navToSignUp.observe(viewLifecycleOwner) {
                if (it != null) {
                    navigate(it)
                }
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