package com.example.note

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

data class Todoitem @RequiresApi(Build.VERSION_CODES.O) constructor(
    val title: String,
    val description: String,
    val alertTime: LocalDateTime,
    val ringtone: String,
    val createdTime: LocalDateTime = LocalDateTime.now()
)
