package com.internshala.notes.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.internshala.notes.data.models.User
import com.internshala.notes.repositories.AuthRepository
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"

sealed class LoginStatus {
    object LoginIsInProgress : LoginStatus()
    object Error : LoginStatus()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _authRepository = AuthRepository.getInstance(application)

    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> = _loginStatus

    fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Update status
            _loginStatus.value = LoginStatus.LoginIsInProgress

            val account = completedTask.getResult(ApiException::class.java)!!

            // Signed in successfully.
            val user = User(
                name = account.displayName ?: "undefined",
                userId = account.id!!,
                picUrl = account.photoUrl.toString(),
                email = account.email
            )
            viewModelScope.launch {
                _authRepository.loginNewUser(user)
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            // Show error
            _loginStatus.value = LoginStatus.Error
        }
    }
}