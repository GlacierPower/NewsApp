package com.newsapp.presentation.views.auth.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.newsapp.R
import com.newsapp.databinding.FragmentSignUpBinding
import com.newsapp.util.NavHelper.navigate
import com.newsapp.util.NavHelper.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()

    private var _viewBinding: FragmentSignUpBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSignUpBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.btnRegistration.setOnClickListener {
            val email = viewBinding.emailRegister.text.toString()
            val pass = viewBinding.passwordRegister.text.toString()
            val confirm = viewBinding.confirmPass.text.toString()
            if (viewModel.signUp(email, pass, confirm)) {
                viewBinding.loadingLayout.visibility = View.VISIBLE
                viewModel.nav.observe(viewLifecycleOwner) {
                    if (it != null) {
                        navigate(it)
                    }
                }
            } else showWarning()
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.navigate()
                showToast(getString(R.string.reg_succ))
            }
        }

        viewModel.exceptionMessage.observe(viewLifecycleOwner) { msg ->
            if (msg.isNotEmpty()) showToast(msg)
        }
    }

    private fun showWarning() {
        if (!viewModel.validateEmail(viewBinding.emailRegister.text.toString())) {
            viewBinding.layoutEmailRegister.error = getString(R.string.invalid_email)
        }
        if (!viewModel.validatePass(viewBinding.passwordRegister.text.toString())) {
            viewBinding.layoutPasswordRegister.error = getString(R.string.password_wrong)
        }
        if (!viewModel.validateConfirmPassword(
                viewBinding.passwordRegister.text.toString(),
                viewBinding.confirmPass.text.toString()
            )
        ) {
            viewBinding.layoutConfirm.error = getString(R.string.confirm_pass_wrong)
        }
    }

}