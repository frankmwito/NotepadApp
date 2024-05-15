package com.example.note

import kotlinx.coroutines.flow.Flow

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
}
