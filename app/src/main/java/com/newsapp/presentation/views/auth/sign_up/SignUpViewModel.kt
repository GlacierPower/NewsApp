package com.newsapp.presentation.views.auth.sign_up

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.R
import com.newsapp.domain.sing_up.SignUpInteractor
import com.newsapp.util.Constants.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpInteractor: SignUpInteractor) :
    ViewModel() {

    private val _exceptionMessage: MutableLiveData<String> = MutableLiveData()
    val exceptionMessage: LiveData<String>
        get() = _exceptionMessage
    private val _isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    private var _signUp = MutableLiveData<Boolean>()
    val signUp: LiveData<Boolean> get() = _signUp

    private var _nav = MutableLiveData<Int?>()
    val nav: LiveData<Int?> get() = _nav

    private var _progressBar = MutableLiveData<Int>()
    val progressBar: LiveData<Int> get() = _progressBar

    fun showProgressBar(){
        _progressBar.postValue(View.VISIBLE)
    }
    fun signUp(email: String, password: String, confirmPassword: String) {
        if (validateEmail(email) && validatePass(password) && validateConfirmPassword(
                password,
                confirmPassword
            )
        ) {
            viewModelScope.launch {
                signUpInteractor.createUser(email, password) { isSuccessful, message ->
                    _isSuccess.value = isSuccessful
                    _exceptionMessage.value = message
                }
            }
            _signUp.value = true
        } else _signUp.value = false

    }


    fun navigate() {
        _nav.value = R.id.signInFragment
    }

    fun validateEmail(email: String): Boolean {
        var isValid = true
        if (email.isNullOrEmpty()) {
            isValid = false
        }
        if (email?.isEmailValid() == false) {
            isValid = false

        }
        return isValid
    }

    fun validatePass(password: String): Boolean {
        var isValid = true
        if (password.isNullOrEmpty()) {
            isValid = false

        }
        if (password.length < 6) {
            isValid = false

        }
        return isValid
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        var isValid = true
        if (confirmPassword.isNullOrEmpty()) {
            isValid = false

        }

        if (confirmPassword != password) {
            isValid = false

        }
        return isValid
    }
}