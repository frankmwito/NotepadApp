package com.example.note

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class NotesViewModel : ViewModel() {
    private val _notesLiveData = MutableLiveData<List<Note>>()
    val notesLiveData: LiveData<List<Note>> = _notesLiveData

    fun addNoteAndSave(context: Context, note: Note) {
        val currentNotes = _notesLiveData.value.orEmpty().toMutableList()
        currentNotes.add(note)
        saveNotes(context, currentNotes)
    }

    fun deleteNoteAndSave(context: Context, note: Note) {
        val currentNotes = _notesLiveData.value.orEmpty().toMutableList()
        currentNotes.remove(note)
        saveNotes(context, currentNotes)
    }

    private fun saveNotes(context: Context, notes: List<Note>) {
        val sharedPreferences = context.getSharedPreferences("notes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(notes)
        editor.putString("notes", json)
        editor.apply()

        _notesLiveData.value = notes // Update LiveData
    }

    fun loadNotes(context: Context) {
        val sharedPreferences = context.getSharedPreferences("notes", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("notes", null)
        val type = object : TypeToken<List<Note>>() {}.type
        val notes = gson.fromJson<List<Note>>(json, type) ?: emptyList()
        _notesLiveData.value = notes
    }
}
