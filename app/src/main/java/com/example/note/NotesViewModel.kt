package com.example.note

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NotesViewModel : ViewModel() {
    val notes = mutableStateListOf<Note>()

    fun addNote(title: String, body: String, context: Context) {
        val timestamp = System.currentTimeMillis()
        val note = Note(title, body, timestamp)
        notes.add(note)
        saveNotes(context, notes)
    }

    fun deleteNote(note: Note, context: Context) {
        notes.remove(note)
        saveNotes(context, notes)
    }

    fun addNoteAndSave(title: String, body: String, context: Context) {
        val timestamp = System.currentTimeMillis()
        val note = Note(title, body, timestamp)
        notes.add(note)
        saveNotes(context, notes)
    }

    fun deleteNoteAndSave(note: Note, context: Context) {
        notes.remove(note)
        saveNotes(context, notes)
    }

}