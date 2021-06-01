package com.internshala.notes.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.internshala.notes.data.models.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE userCreatorUid = :userId AND status = ${Note.NOTE_STATUS_IDEAL} ORDER BY id DESC")
    fun getUserNotes(userId: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE userCreatorUid = :userId AND status = ${Note.NOTE_STATUS_ARCHIVED} ORDER BY id DESC")
    fun getArchivedUserNotes(userId: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE userCreatorUid = :userId AND status = ${Note.NOTE_STATUS_DELETED} ORDER BY id DESC")
    fun getBinnedUserNotes(userId: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%'")
    suspend fun searchNotesByTitle(query: String): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}