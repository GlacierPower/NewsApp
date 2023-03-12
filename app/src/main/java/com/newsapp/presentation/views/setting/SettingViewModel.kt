package com.newsapp.presentation.views.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.newsapp.R
import com.newsapp.data.sharedpreferences.UIMode
import com.newsapp.domain.news.NewsInteractor
import com.newsapp.domain.sign_out.SignOutInteractor
import com.newsapp.util.InternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val newsInteractor: NewsInteractor,
    private val signOutInteractor: SignOutInteractor,
) : ViewModel() {

    val theme = newsInteractor.uIModeFlow()

    @Inject
    lateinit var internetConnection: InternetConnection

    private var _navSearch = MutableLiveData<Int?>()
    val navSearch: LiveData<Int?> = _navSearch

    private var _navFav = MutableLiveData<Int?>()
    val navFav: LiveData<Int?> = _navFav

    private var _navLogin = MutableLiveData<Int?>()
    val navLogin: LiveData<Int?> get() = _navLogin

    private var _auth = MutableLiveData<Boolean>()
    val auth: LiveData<Boolean> get() = _auth

    private var _connect = MutableLiveData<Boolean>()
    val connect: LiveData<Boolean> get() = _connect

    fun connect() {
        _connect.value = internetConnection.isOnline()
    }

    fun signOut() {
        viewModelScope.launch {
            signOutInteractor.invoke()
        }

    }

    fun navigateToLogin() {
        _navLogin.value = R.navigation.auth_graph
    }

    fun navigateToFavorite() {
        _navFav.value = R.id.action_settingFragment_to_saveFragment
    }

    fun navigateToSearch() {
        _navSearch.value = R.id.action_settingFragment_to_searchFragment
    }


    fun userNavigated() {
        _navSearch.value = null
    }

    fun isUserLoggedIn() {
        _auth.value = FirebaseAuth.getInstance().currentUser == null
    }

    fun setMode(uiMode: UIMode) {
        viewModelScope.launch { newsInteractor.setDarkMode(uiMode) }

    }

}
