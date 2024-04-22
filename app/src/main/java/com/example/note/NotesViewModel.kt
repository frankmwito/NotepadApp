package com.example.note

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NotesViewModel : ViewModel() {
    val notes = mutableStateListOf<Note>()

    fun addNote(title: String, body: String) {
        val timestamp = System.currentTimeMillis()
        val note = Note(title, body, timestamp)
        notes.add(note)
    }

    fun deleteNote(note: Note) {
        notes.remove(note)
    }
}