package com.internshala.notes.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.internshala.notes.data.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id LIKE :userId")
    suspend fun getUserById(userId: String)

    @Query("SELECT * FROM users ORDER BY id ASC LIMIT 1")
    suspend fun getFirstUserFromUsers(): List<User>

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<User>>
}