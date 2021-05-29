package com.internshala.notes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.internshala.notes.data.models.User
import com.internshala.notes.repositories.AuthRepository
import com.internshala.notes.repositories.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val _authRepository = AuthRepository.getInstance(application)

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

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