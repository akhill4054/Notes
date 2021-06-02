package com.internshala.notes.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.internshala.notes.data.db.AppDatabase
import com.internshala.notes.data.db.dao.NoteDao
import com.internshala.notes.data.models.Note
import com.internshala.notes.data.models.User
import java.util.*

class NotesRepository(application: Application) {
    private val _noteDao: NoteDao

    init {
        val db = AppDatabase.getDatabase(application)
        _noteDao = db.noteDao()
    }

    suspend fun insert(note: Note) {
        _noteDao.insert(note)
    }

    fun getUserNotes(user: User): LiveData<List<Note>> {
        return _noteDao.getUserNotes(user.userId)
    }

    fun getArchivedUserNotes(user: User): LiveData<List<Note>> {
        return _noteDao.getArchivedUserNotes(user.userId)
    }

    suspend fun getBinnedUserNotesAsync(user: User): List<Note> {
        return _noteDao.getBinnedUserNotesAsync(user.userId)
    }

    fun getBinnedUserNotes(user: User): LiveData<List<Note>> {
        return _noteDao.getBinnedUserNotes(user.userId)
    }

    suspend fun searchNotesByTitle(query: String): List<Note> {
        return _noteDao.searchNotesByTitle(query)
    }

    suspend fun update(note: Note) {
        _noteDao.update(note)
    }

    suspend fun archive(note: Note) {
        _noteDao.update(note.copy(status = Note.NOTE_STATUS_ARCHIVED))
    }

    suspend fun unarchive(note: Note) {
        _noteDao.update(note.copy(status = Note.NOTE_STATUS_IDEAL))
    }

    suspend fun moveToBin(note: Note) {
        val now = Calendar.getInstance().time

        _noteDao.update(
            note.copy(
                status = Note.NOTE_STATUS_DELETED,
                deletedOn = now
            )
        )
    }

    suspend fun delete(note: Note) {
        _noteDao.delete(note)
    }

    suspend fun restore(note: Note) {
        _noteDao.update(
            note.copy(
                status = Note.NOTE_STATUS_IDEAL,
                deletedOn = null
            )
        )
    }

    companion object {
        @Volatile
        private var INSTANCE: NotesRepository? = null

        fun getInstance(application: Application): NotesRepository {
            var localRef = INSTANCE // To reduce read on volatile field

            return localRef ?: synchronized(NotesRepository::class.java) {
                localRef = INSTANCE // To reduce read on volatile field
                localRef ?: NotesRepository(application).also {
                    // Initializing static field
                    INSTANCE = it
                }
            }
        }
    }
}