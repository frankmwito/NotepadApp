package com.example.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Entity(tableName = "todoitems")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    @TypeConverters(Converters::class)val alertTime: LocalDateTime?,
    val ringtone: String,
    val completed: Boolean = false
)
