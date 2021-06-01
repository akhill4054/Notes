package com.internshala.notes.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.internshala.notes.data.models.User
import com.internshala.notes.data.models.UserWithNotes

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id LIKE :userId")
    suspend fun getUserById(userId: String): User?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserWithNotesById(id: Long): UserWithNotes

    @Query("SELECT * FROM users ORDER BY id ASC LIMIT 1")
    suspend fun getFirstUserFromUsers(): User?

    @Insert
    suspend fun insert(user: User)

    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun deleteByUserId(userId: String)

    @Query("SELECT COUNT(userId) FROM users WHERE userId = :userId")
    suspend fun getCountByUserId(userId: String): Int

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE userId != :currentUserUid ORDER BY id ASC")
    fun getOtherUsers(currentUserUid: String): LiveData<List<User>>
}