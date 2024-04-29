package com.example.note

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class HomeScreen : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val viewModel: NotesViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Home_Screen(viewModel = viewModel)
        Spacer(modifier = Modifier.height( 8.dp))

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@Composable
fun Home_Screen(viewModel: NotesViewModel) {
    val ctx = LocalContext.current
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

            val greeting = when (currentHour) {
                in 0..11 -> "Good Morning"
                in 12..17 -> "Good Afternoon"
                else -> "Good Evening"
            }

            val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
            val dateString = dateFormat.format(java.util.Date())

            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = greeting,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )

                        Text(
                            text = "User's Name",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                        Text(
                            text = dateString,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )

                    }
                    Spacer(modifier = Modifier.width((85).dp))
                    Column(
                        horizontalAlignment = AbsoluteAlignment.Left
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter Icon"
                            )
                        }
                    }
                    Column {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings Icon"
                            )

                        }

                    }
                }
                Row(
                    modifier = Modifier.offset(y = 70.dp)

                ) {
                    Notepad()
                }
                Row(
                    modifier = Modifier
                        .offset(y = (530).dp) // adjust the value to move the FAB up
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    horizontalArrangement = Arrangement.Absolute.Right
                ) {
                    FloatingActionButton(
                        onClick = {  val intent = Intent(ctx, Notesadd::class.java)
                            ctx.startActivity(intent) },
                        containerColor = Color.Black,
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                }
            }
            NotesList(viewModel = viewModel)
        }
    }

    @Composable
    fun Notepad(
        color: Color = Color.Black
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(color)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Notepad",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }


// Notecard composable
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteCard(note: Note, viewModel: NotesViewModel) {
    Log.d("Notecard", "Displaying note: $note")
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .background(color = Color.Gray)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = note.title,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.body,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = LocalDateTime.ofInstant(Instant.ofEpochMilli(note.timestamp), ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")),
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EditButton(note = note, viewModel = viewModel)
                DeleteButton(note = note, viewModel = viewModel)
            }
        }
    }
}


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun EditButton(note: Note, viewModel: NotesViewModel) {
    IconButton(
        onClick = {
// Call the updateNote function from a coroutine context
            GlobalScope.launch {
                viewModel.updateNote(
                    Note(
                        id = note.id,
                        title = "Updated Title",
                        body = "Updated Body",
                        timestamp = note.timestamp
                    )
                )
            }
        },
        modifier = Modifier.size(20.dp, 20.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            tint = Color.Blue
        )
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun DeleteButton(note: Note, viewModel: NotesViewModel) {
    IconButton(
        onClick = {
            // Call the deleteNote function from a coroutine context
            GlobalScope.launch {
                viewModel.deleteNote(note)
            }
        },
        modifier = Modifier.size(20.dp, 20.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.Red
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesList(viewModel: NotesViewModel) {
    val notes: List<Note>? by viewModel.notes.observeAsState()

    // Use viewModelScope
    LaunchedEffect(Unit) {
        viewModel.viewModelScope.launch {
            notes?.forEach { note ->
                viewModel.deleteNote(note)
            }
        }
    }

    LazyColumn {
        items(notes ?: emptyList()) { note ->
            NoteCard(note = note, viewModel = viewModel)
        }
    }
}





