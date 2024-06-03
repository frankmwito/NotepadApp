package com.example.note

import androidx.room.PrimaryKey

@androidx.room.Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val body: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)

