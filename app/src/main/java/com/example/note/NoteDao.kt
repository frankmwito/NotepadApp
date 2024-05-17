package com.example.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotesFlow(): Flow<List<Note>>

    @Query("""
        SELECT * FROM notes 
        WHERE title LIKE :query OR 
              body LIKE :query OR 
              strftime('%Y-%m-%d %H:%M:%S', datetime(timestamp / 1000, 'unixepoch')) LIKE :query
    """)
    fun searchNotes(query: String): List<Note>
}
