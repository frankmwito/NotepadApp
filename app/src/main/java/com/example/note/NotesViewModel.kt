package com.example.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = AppDatabase.getInstance(application).noteDao()
    val notes: LiveData<List<Note>> = noteDao.getAllNotes()

    // Use Room's Flow extension function to convert to LiveData

    // Coroutine-Safe insert function
    suspend fun insertNote(title: String, body: String) {
        withContext(Dispatchers.IO) {
            val note = Note(id = 0, title = title, body = body)
            noteDao.insert(note)
        }
    }

    // Coroutine-Safe delete function
    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.delete(note)
        }
    }

    // Regular update function with coroutine
    suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            noteDao.update(note)
        }
    }
}
/*Note that the `saveNotes` function now updates the `_notesLiveData` variable instead of the `_notes` variable.*/