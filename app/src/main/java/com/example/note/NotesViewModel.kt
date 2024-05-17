package com.example.note

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Clock

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository = AppDatabase.provideNoteRepository(application)
    val notes: StateFlow<List<Note>> = noteRepository.getAllNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNote(title: String, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = Clock.systemDefaultZone().instant()
            val note = Note(id = 0, title = title, body = body, timestamp = currentTime.toEpochMilli())
            noteRepository.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.delete(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.update(note)
        }
    }

    private val _searchResults = MutableStateFlow<List<Note>>(emptyList())
    val searchResults: StateFlow<List<Note>> = _searchResults

    fun searchNotes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchResults.value = noteRepository.searchNotes(query)
        }
    }

    private val _sortedNotes = MutableStateFlow<List<Note>>(emptyList())
    val sortedNotes: StateFlow<List<Note>> = _sortedNotes

    init {
        sortNotes("timestamp", NoteRepository.SortOrder.ASC) // Default sort
    }

    fun sortNotes(sortBy: String, sortOrder: NoteRepository.SortOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.sortNotes(sortBy, sortOrder).collect { sortedNotes ->
                _sortedNotes.value = sortedNotes
            }
        }
    }

    fun shareNote(note: Note): Intent {
        return noteRepository.shareNote(note)
    }
}
