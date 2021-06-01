package com.internshala.notes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.internshala.notes.data.models.Note
import com.internshala.notes.repositories.AuthRepository
import com.internshala.notes.repositories.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

sealed class NoteEditStatus
object Ideal : NoteEditStatus()
object NoteSaved : NoteEditStatus()
object NoteModified : NoteEditStatus()
object EmptyNoteDiscarded : NoteEditStatus()

class EditNoteViewModel(application: Application) : AndroidViewModel(application) {
    private val _authRepository = AuthRepository.getInstance(application)
    private val _notesRepository = NotesRepository.getInstance(application)

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> = _note

    private val _noteEditStatus = MutableLiveData<NoteEditStatus>()
    val noteEditStatus: LiveData<NoteEditStatus> = _noteEditStatus

    fun resetStatus() {
        _noteEditStatus.value = Ideal
    }

    fun setCurrentEditableNote(note: Note?) {
        if (note != null) {
            _note.value = note!!
        } else {
            val user = _authRepository.getCurrentUser()!!
            _note.value = Note(
                userCreatorUid = user.userId,
            )
        }
    }

    fun saveNote(title: String, noteText: String) {
        val oldNote = _note.value!!

        if ((title.isNotEmpty() || noteText.isNotEmpty())) {
            if ((oldNote.title != title || oldNote.note != noteText)) {
                viewModelScope.launch(Dispatchers.IO) {
                    val now = Calendar.getInstance().time

                    val note = oldNote.copy(
                        title = title,
                        note = noteText,
                        lastModified = now,
                    )

                    if (note.createdOn == null) {
                        // New note
                        _notesRepository.insert(
                            note.copy(createdOn = now)
                        )

                        _noteEditStatus.postValue(NoteSaved)
                    } else {
                        // Update note
                        _notesRepository.update(note)

                        _noteEditStatus.postValue(NoteModified)
                    }
                }
            }
        } else {
            _noteEditStatus.value = EmptyNoteDiscarded
        }
    }

    private fun discardFreshNote() {
        _noteEditStatus.postValue(EmptyNoteDiscarded)
    }

    fun archive(note: Note) {
        if (note.isFreshNote()) {
            discardFreshNote()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _notesRepository.archive(note)
            }
        }
    }

    fun unarchive(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            _notesRepository.unarchive(note)
        }
    }

    fun bin(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            _notesRepository.moveToBin(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            _notesRepository.delete(note)
        }
    }

    fun restore(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            _notesRepository.restore(note)
        }
    }
}