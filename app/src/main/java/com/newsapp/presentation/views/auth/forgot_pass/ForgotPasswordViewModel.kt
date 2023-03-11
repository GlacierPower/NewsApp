package com.newsapp.presentation.views.auth.forgot_pass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.R
import com.newsapp.domain.sign_in.SignInInteractor
import com.newsapp.util.Constants.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val signInInteractor: SignInInteractor) :
    ViewModel() {

    private val _exceptionMessage: MutableLiveData<String> = MutableLiveData()
    val exceptionMessage: LiveData<String>
        get() = _exceptionMessage
    private val _isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    private var _nav = MutableLiveData<Int?>()
    val nav: LiveData<Int?> get() = _nav

    fun resetPassword(email: String): Boolean {
        return if (validation(email)) {
            viewModelScope.launch {
                signInInteractor.sendPasswordResetEmail(email) { isSuccess, message ->
                    _isSuccess.value = isSuccess
                    _exceptionMessage.value = message
                }
            }
            true
        } else false
    }

    fun navigate() {
        _nav.value = R.id.action_forgotPasswordFragment_to_signInFragment
    }

    fun validation(email: String): Boolean {
        var isValid = true
        if (email.isNullOrEmpty()) {
            isValid = false

        }
        if (email.isEmailValid() == false) {
            isValid = false

        }
        return isValid
    }

}