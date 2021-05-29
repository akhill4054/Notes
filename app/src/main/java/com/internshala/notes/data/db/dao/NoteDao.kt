package com.internshala.notes.data.db.dao

import androidx.room.*
import com.internshala.notes.data.models.Note
import com.internshala.notes.data.models.User
import com.internshala.notes.data.models.UserWithNotes

@Dao
interface NoteDao {
    @Query("SELECT * FROM users WHERE id LIKE :id")
    suspend fun getUserById(id: Long)

    @Query("SELECT * FROM notes WHERE id LIKE :id ORDER BY id ASC")
    suspend fun getAllNotesByUserId(id: Long): List<User>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserWithNotesById(id: Long): UserWithNotes

    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)
}