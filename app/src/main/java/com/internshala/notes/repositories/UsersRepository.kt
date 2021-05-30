package com.internshala.notes.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.internshala.notes.data.db.AppDatabase
import com.internshala.notes.data.db.dao.UserDao
import com.internshala.notes.data.models.User

class UsersRepository private constructor(application: Application) {

    private val _userDao: UserDao

    init {
        val db = AppDatabase.getDatabase(application)
        _userDao = db.userDao()
    }

    suspend fun insertUser(user: User) {
        _userDao.insert(user)
    }

    suspend fun removeUser(user: User) {
        _userDao.deleteByUserId(user.userId)
    }

    suspend fun getFirstUserFromUsers(): User? {
        return _userDao.getFirstUserFromUsers()
    }

    fun getAllUsers(): LiveData<List<User>> = _userDao.getAllUsers()

    companion object {
        @Volatile
        private var INSTANCE: UsersRepository? = null

        fun getInstance(application: Application): UsersRepository {
            var localRef = INSTANCE // To reduce read on volatile field

            return localRef ?: synchronized(UsersRepository::class.java) {
                localRef = INSTANCE // To reduce read on volatile field
                localRef ?: UsersRepository(application).also {
                    // Initializing static field
                    INSTANCE = it
                }
            }
        }
    }
}