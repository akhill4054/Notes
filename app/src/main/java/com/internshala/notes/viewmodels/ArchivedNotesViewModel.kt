package com.internshala.notes.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.internshala.notes.data.models.Note
import com.internshala.notes.repositories.AuthRepository
import com.internshala.notes.repositories.Authenticated
import com.internshala.notes.repositories.NotesRepository
import com.internshala.notes.repositories.OnUserChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ArchivedNotesViewModel(application: Application) : AndroidViewModel(application) {

    private val _authRepository = AuthRepository.getInstance(application)
    private val _notesRepository = NotesRepository.getInstance(application)

    val archivedNotes: LiveData<List<Note>> = _authRepository.authStatus.switchMap { authStatus ->
        when (authStatus) {
            is Authenticated -> {
                _notesRepository.getArchivedUserNotes(authStatus.user)
            }
            is OnUserChanged -> {
                _notesRepository.getArchivedUserNotes(authStatus.user)
            }
            else -> {
                MutableLiveData()
            }
        }
    }
}