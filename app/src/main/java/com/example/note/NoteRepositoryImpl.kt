package com.example.note

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotesFlow()

    override suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    override suspend fun update(note: Note) {
        noteDao.update(note)
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    override fun searchNotes(query: String): List<Note> {
        return noteDao.searchNotes("%$query%")
    }

    override fun shareNote(note: Note): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${note.title}\n${note.body}")
        return shareIntent
    }

    override fun sortNotes(sortBy: String, sortOrder: NoteRepository.SortOrder): Flow<List<Note>> {
        return when (sortBy) {
            "title" -> {
                if (sortOrder == NoteRepository.SortOrder.ASC) {
                    noteDao.getAllNotesFlow().map { notes -> notes.sortedBy { it.title } }
                } else {
                    noteDao.getAllNotesFlow().map { notes -> notes.sortedByDescending { it.title } }
                }
            }
            "body" -> {
                if (sortOrder == NoteRepository.SortOrder.ASC) {
                    noteDao.getAllNotesFlow().map { notes -> notes.sortedBy { it.body } }
                } else {
                    noteDao.getAllNotesFlow().map { notes -> notes.sortedByDescending { it.body } }
                }
            }
            "timestamp" -> {
                if (sortOrder == NoteRepository.SortOrder.ASC) {
                    noteDao.getAllNotesFlow().map { notes -> notes.sortedBy { it.timestamp } }
                } else {
                    noteDao.getAllNotesFlow().map { notes -> notes.sortedByDescending { it.timestamp } }
                }
            }
            else -> throw IllegalArgumentException("Invalid sort by field")
        }
    }
}

