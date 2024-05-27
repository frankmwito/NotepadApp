package com.example.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class TodoList: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           Todolist()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Todolist() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        darkIcons = true // Set true if using a light status bar
    )
    systemUiController.setNavigationBarColor(
        color = Color.Transparent,
        darkIcons = true // Set true if using a light navigation bar
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "To-Do-List",
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Sort"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        paddingValues
        val ctx = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 75.dp), // Add padding to keep FAB above navigation bar
            contentAlignment = Alignment.BottomEnd
        ) {
            ExtendedFloatingActionButton(
                text = { Text(text = "New Task", color = Color.Black) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.NoteAdd,
                        contentDescription = "Create a new Task",
                        tint = Color.Black
                    )
                },
                onClick = {
                },
                containerColor = Color(0xFFCCC2DC),
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        todolist()
    }
}
@Composable
fun todolist() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .background(color = Color.Transparent)
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { }

    ) {
        Column {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Title")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Description")
                Spacer(modifier = Modifier.height(8.dp))
            }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(color = Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            RadioButton(selected = true, onClick = { /*TODO*/ },
                modifier = Modifier.padding(2.dp),
                colors = RadioButtonDefaults.colors(unselectedColor = Color.White,
                    selectedColor = Color(0xFFCCC2DC), disabledSelectedColor = Color.Transparent
                ))
        }

    }
}
}


