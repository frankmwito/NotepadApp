package com.example.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository = AppDatabase.provideNoteRepository(application)
    val notes: StateFlow<List<Note>> = noteRepository.getAllNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun insertNote(title: String, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(id = 0, title = title, body = body)
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

    // Function to search notes
    fun searchNotes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchResults.value = noteRepository.searchNotes(query)
        }
    }
}