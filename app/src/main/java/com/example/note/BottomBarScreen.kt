package com.example.note

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomBarScreen (
    val route: String,
    val title: String,
    val icon: ImageVector,
) {

    HOME("home","Home", Icons.Default.Home),
    NOTEPAD("Notepad", "Notepad", Icons.Default.NoteAdd),
    SHEDULES("Schedules", "Schedules", Icons.Default.Timer);

}