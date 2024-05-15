package com.example.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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
}
/*Note that the `saveNotes` function now updates the `_notesLiveData` variable instead of the `_notes` variable.*/