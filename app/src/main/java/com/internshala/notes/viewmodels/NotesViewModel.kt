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

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val _authRepository = AuthRepository.getInstance(application)
    private val _notesRepository = NotesRepository.getInstance(application)

    val userNotes: LiveData<List<Note>> = _authRepository.authStatus.switchMap { authStatus ->
        when (authStatus) {
            is Authenticated -> {
                _notesRepository.getUserNotes(authStatus.user)
            }
            is OnUserChanged -> {
                _notesRepository.getUserNotes(authStatus.user)
            }
            else -> {
                MutableLiveData()
            }
        }
    }

    private val _searchResult = MutableLiveData<List<Note>>()
    val searchResult: LiveData<List<Note>> = _searchResult

    private var lastSearchJob: Job? = null

    fun searchNotes(query: String?) {
        if (query.isNullOrEmpty()) {
            // Reset notes
            _searchResult.value = userNotes.value
        } else {
            lastSearchJob?.cancel()
            lastSearchJob = viewModelScope.launch(Dispatchers.IO) {
                // Wait for query change before search
                delay(200)
                _searchResult.postValue(_notesRepository.searchNotesByTitle(query))
            }
        }
    }
}