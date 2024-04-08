package com.example.note

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)
    val listOfNavItems : List<NavItem> = listOf(
        NavItem(
            label = "Home",
            icon = Icons.Default.Home,
            route = Screens.HomeScreen.name
        ),
        NavItem(
            label = "ToDoList",
            icon = Icons.Default.NoteAdd,
            route = Screens.TodoList.name
        ),
        NavItem(
            label = "Schedules",
            icon = Icons.Default.Timer,
            route = Screens.Shedular.name
        )
    )

