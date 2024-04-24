package com.example.note

data class Note(
    val id: Long = 0L,
    val title: String,
    val body: String,
    val timestamp: Long
)
