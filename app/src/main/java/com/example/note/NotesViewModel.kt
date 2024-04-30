package com.example.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = AppDatabase.getInstance(application).noteDao()
    val notes: LiveData<List<Note>> = noteDao.getAllNotes()
    // Coroutine-Safe insert function
    fun insertNote(title: String, body: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(id = 0, title = title, body = body)
            noteDao.insert(note)
        }
    }

    // Coroutine-Safe delete function
    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    // Regular update function with coroutine
    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.update(note)
        }
    }
}
/*Note that the `saveNotes` function now updates the `_notesLiveData` variable instead of the `_notes` variable.*/