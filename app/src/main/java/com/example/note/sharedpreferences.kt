package com.example.note
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Save notes to SharedPreferences
fun saveNotes(context: Context, notes: List<Note>) {
    val sharedPreferences = context.getSharedPreferences("notes", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val json = gson.toJson(notes)
    editor.putString("notes", json)
    editor.apply()
}

// Load notes from SharedPreferences
fun loadNotes(context: Context): List<Note> {
    val sharedPreferences = context.getSharedPreferences("notes", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = sharedPreferences.getString("notes", null)
    val type = object : TypeToken<List<Note>>() {}.type
    return gson.fromJson(json, type) ?: emptyList()
}
