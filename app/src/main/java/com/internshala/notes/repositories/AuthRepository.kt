package com.internshala.notes.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.internshala.notes.data.models.User
import com.internshala.notes.utils.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Auth status
sealed class AuthStatus
object Unauthenticated : AuthStatus()
class Authenticated(val user: User) : AuthStatus()
class OnUserChanged(val user: User) : AuthStatus()

class AuthRepository private constructor(application: Application) {

    private val _application = application

    private val _authStatus = MutableLiveData<AuthStatus>()
    val authStatus: LiveData<AuthStatus> = _authStatus

    init {
        // Setting up auth status
        val prefs = PreferenceManager.getInstance(application)
        val currentUser = prefs.getCurrentUser()

        if (currentUser != null) {
            _authStatus.value = Authenticated(user = currentUser)
        } else {
            _authStatus.value = Unauthenticated
        }
    }

    private fun isAuthenticated(): Boolean = _authStatus.value is Authenticated

    suspend fun loginNewUser(user: User) {
        // Save the new user in prefs
        val prefs = PreferenceManager.getInstance(_application)
        prefs.setCurrentUser(user)

        withContext(Dispatchers.IO) {
            // Save the new user in db
            val userRepository = UsersRepository.getInstance(_application)

            withContext(Dispatchers.IO) {
                userRepository.insertUser(user)
            }
        }

        // Update auth status
        if (isAuthenticated()) {
            _authStatus.postValue(OnUserChanged(user))
        } else {
            _authStatus.postValue(Authenticated(user))
        }
    }

    fun switchUser(newUser: User) {
        // Update prefs
        val prefs = PreferenceManager.getInstance(_application)
        prefs.setCurrentUser(newUser)

        // Update auth status
        _authStatus.postValue(OnUserChanged(newUser))
    }

    fun getCurrentUser(): User? {
        return when (val status = authStatus.value) {
            is Authenticated -> status.user
            is OnUserChanged -> status.user
            else -> null
        }
    }

    suspend fun removeCurrentUser() {
        val userRepository = UsersRepository.getInstance(_application)

        val prefs = PreferenceManager.getInstance(_application)
        val user = prefs.getCurrentUser()

        user?.let {
            // Remove from prefs
            prefs.setCurrentUser(null)

            var nextUser: User?

            // Remove from db
            withContext(Dispatchers.IO) {
                userRepository.removeUser(user)
                // Try to get the next user
                nextUser = userRepository.getFirstUserFromUsers()
            }

            if (nextUser != null) {
                switchUser(nextUser!!)
            } else {
                _authStatus.postValue(Unauthenticated)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null

        fun getInstance(application: Application): AuthRepository {
            var localRef = INSTANCE // To reduce read on volatile field

            return localRef ?: synchronized(AuthRepository::class.java) {
                localRef = INSTANCE // To reduce read on volatile field
                localRef ?: AuthRepository(application).also {
                    // Initializing static field
                    INSTANCE = it
                }
            }
        }
    }
}