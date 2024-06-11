package com.example.note

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note.ui.theme.NoteTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class Noteadd : ComponentActivity() {
    private val viewModel: NotesViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteTheme {
                NotesAdd(
                    viewModel = viewModel,
                    onBackPressed = { onBackPressed() }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesAdd(viewModel: NotesViewModel, onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val title = remember { mutableStateOf("") }
    val body = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("Journal") } // Default category
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )
        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = true
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Note",
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (title.value.isBlank() || category.value.isBlank()) {
                            Toast.makeText(context, "Title and Category cannot be empty", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.insertNote(
                                title = title.value,
                                body = body.value,
                                category = category.value
                            )
                            onBackPressed()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.SaveAlt,
                            contentDescription = "Save"
                        )
                    }
                }
            )
        },
        content = {
                innerPadding -> 
                Column(
                    modifier = Modifier.padding(innerPadding)
                ){
                    CategorySelection(category)
                    BodyContent(title = title, body = body, category = category)
                }
            
        }
    )
}

@Composable
fun BodyContent(title: MutableState<String>, body: MutableState<String>, category: MutableState<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = {
                Text(
                    "Title",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.SansSerif
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            TextField(
                value = body.value,
                onValueChange = { body.value = it },
                label = { Text(text = "Body", fontSize = 16.sp, color = Color.Black, fontStyle = FontStyle.Italic, fontFamily = FontFamily.SansSerif) },
                placeholder = {
                    Text(
                        text = "Note something down.....",
                        fontSize = 13.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CategorySelection(category: MutableState<String>) {
    val categories = listOf("Journals", "Notes")
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        TextButton(onClick = { expanded = true }) {
            Text(
                text = "category",
                fontSize = 16.sp,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.SansSerif
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Toggle Dropdown")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            categories.forEach { categoryText ->
                DropdownMenuItem(onClick = {
                    category.value = categoryText
                    expanded = false
                }) {
                    Text(
                        text = categoryText,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }
    }
}


