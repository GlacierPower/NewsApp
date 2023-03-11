package com.newsapp.presentation.views.auth.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.R
import com.newsapp.domain.sign_in.SignInInteractor
import com.newsapp.util.Constants.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val signInInteractor: SignInInteractor) :
    ViewModel() {

    private val _exceptionMessage: MutableLiveData<String> = MutableLiveData()
    val exceptionMessage: LiveData<String>
        get() = _exceptionMessage
    private val _isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    private var _nav = MutableLiveData<Int?>()
    val nav: LiveData<Int?> get() = _nav

    private var _navToSignUp = MutableLiveData<Int?>()
    val navToSignUp: LiveData<Int?> get() = _navToSignUp

    private var _navToForgot = MutableLiveData<Int?>()
    val navToForgot get() = _navToForgot

    fun signIn(email: String, password: String): Boolean {
        return if (validateEmail(email) && validatePassword(password)) {
            viewModelScope.launch {
                signInInteractor.signIn(email, password) { isSuccessful, message ->
                    _isSuccess.value = isSuccessful
                    _exceptionMessage.value = message
                }
            }
            true
        } else false
    }

    fun replaceGraph() {
        _nav.value = R.navigation.nav_graph
    }

    fun navigateToSignUp() {
        _navToSignUp.value = R.id.action_signInFragment_to_signUpFragment
    }

    fun navigateToForgotPassword() {
        _navToForgot.value = R.id.action_signInFragment_to_forgotPasswordFragment
    }

     fun validateEmail(email: String): Boolean {
        var isValid = true
        if (email.isNullOrEmpty()) {
            isValid = false

        }
        if (!email.isEmailValid()) {
            isValid = false

        }

        return isValid
    }

     fun validatePassword(password: String): Boolean {
        var isValid = true
        if (password.isNullOrEmpty()) {
            isValid = false

        }
        if ((password.length ?: 0) < 6) {
            isValid = false

        }
        return isValid
    }
}