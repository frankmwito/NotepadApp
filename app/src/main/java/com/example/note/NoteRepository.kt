package com.example.note

import android.content.Intent
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
    fun shareNote(note: Note): Intent
    fun searchNotes(query: String): List<Note>
    enum class SortOrder { ASC, DESC }
    fun sortNotes(sortBy: String, sortOrder: SortOrder): Flow<List<Note>>
}
