package com.internshala.notes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.internshala.notes.data.models.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE id LIKE :id")
    suspend fun getNoteById(id: Long): Note

    @Query("SELECT * FROM notes WHERE id LIKE :id ORDER BY id ASC")
    suspend fun getAllNotesByUserId(id: Long): List<Note>

    @Insert
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)
}