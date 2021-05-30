package com.internshala.notes.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.internshala.notes.data.models.User
import com.internshala.notes.repositories.AuthRepository
import com.internshala.notes.repositories.Authenticated
import com.internshala.notes.repositories.OnUserChanged
import com.internshala.notes.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val _authRepository = AuthRepository.getInstance(application)

    val user: LiveData<User?> = _authRepository.authStatus.map { authStatus ->
        when (authStatus) {
            is Authenticated -> authStatus.user
            is OnUserChanged -> authStatus.user
            else -> null
        }
    }

    private val _allUsers = MutableLiveData<List<User>>()
    val allUsers: LiveData<List<User>> = _allUsers

    fun switchUser(user: User) {
        _authRepository.switchUser(user)
    }

    fun logout(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            _authRepository.logout(user)
        }
    }
}