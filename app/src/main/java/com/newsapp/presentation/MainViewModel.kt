package com.newsapp.presentation

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.newsapp.R
import com.newsapp.data.sharedpreferences.UIMode
import com.newsapp.domain.dark_mode.DarkModeInteractor
import com.newsapp.domain.sign_out.SignOutInteractor
import com.newsapp.util.InternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val darkModeInteractor: DarkModeInteractor,
    private val signOutInteractor: SignOutInteractor
) : ViewModel() {

    @Inject
    lateinit var internetConnection: InternetConnection

    private var _connect = MutableLiveData<Boolean>()
    val connect: LiveData<Boolean> get() = _connect

    private var _navLogin = MutableLiveData<Int?>()
    val navLogin: LiveData<Int?> get() = _navLogin

    private var _auth = MutableLiveData<Boolean>()
    val auth: LiveData<Boolean> get() = _auth

    val theme = darkModeInteractor.uIModeFlow()

    private var _nav = MutableLiveData<Int>()
    val nav: LiveData<Int> get() = _nav

    fun connect() {
        _connect.value = internetConnection.isOnline()
    }

    fun navigateToLogin() {
        _navLogin.value = R.navigation.auth_graph
    }

    fun showBottomNav() {
        _nav.postValue(View.VISIBLE)
    }

    fun hideBottomNav() {
        _nav.postValue(View.GONE)
    }

    fun setMode(uiMode: UIMode) {
        viewModelScope.launch { darkModeInteractor.setDarkMode(uiMode) }

    }

    fun isUserLoggedIn() {
        _auth.value = FirebaseAuth.getInstance().currentUser == null
    }

    fun signOut() {
        viewModelScope.launch {
            signOutInteractor.invoke()
        }

    }
}