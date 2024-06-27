package com.example.note

//noinspection UsingMaterialAndMaterial3Libraries
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.note.ui.theme.NoteTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteTheme {
                    MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val viewModel: NotesViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Home_Screen(viewModel = viewModel)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home_Screen(viewModel: NotesViewModel) {
    var showSearchDialog by remember { mutableStateOf(false) }
    var sortBy by remember { mutableStateOf("timestamp") }
    var sortOrder by remember { mutableStateOf(NoteRepository.SortOrder.ASC) }
    var showSortMenu by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }

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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Notes",
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                },
                actions = {
                    IconButton(onClick = { showSearchDialog = true }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { showSortMenu = !showSortMenu }) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                    }
                    SortDropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        onSortSelected = { newSortBy, newSortOrder ->
                            sortBy = newSortBy
                            sortOrder = newSortOrder
                            viewModel.sortNotes(sortBy, sortOrder)
                        },
                        currentSortBy = sortBy,
                        currentSortOrder = sortOrder
                    )
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                CategoryTabs(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        selectedCategory = category
                        // Filter notes based on category
                    }
                )
                NotesList(viewModel, selectedCategory)
                if (showSearchDialog) {
                    FullScreenSearchDialog(
                        viewModel = viewModel,
                        onDismissRequest = { showSearchDialog = false }
                    )
                }
            }
            Floatingactionbutton()
        }
    )
}

@Composable
fun CategoryTabs(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("All", "Journals", "Notes")
    var selectedTabIndex by remember { mutableStateOf(categories.indexOf(selectedCategory)) }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth(),

    ) {
        categories.forEachIndexed { index, categoryText ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    onCategorySelected(categoryText)
                },
                text = {
                    Text(
                        text = categoryText,
                        fontSize = 16.sp,
                        color = if (selectedTabIndex == index) Color.Black else Color.Gray,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            )
        }
    }
}


@Composable
fun SortDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSortSelected: (String, NoteRepository.SortOrder) -> Unit,
    currentSortBy: String,
    currentSortOrder: NoteRepository.SortOrder
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            onClick = {
                onSortSelected("title", currentSortOrder)
                onDismissRequest()
            },
            text  = {
                Text(text = "Sort by Title")
            }
        )
        DropdownMenuItem(
            onClick = {
                onSortSelected("body", currentSortOrder)
                onDismissRequest()
            },
            text = {
                Text(text = "Sort by Body")
            }
        )
        DropdownMenuItem(
            onClick = {
                onSortSelected("timestamp", currentSortOrder)
                onDismissRequest()
            },
            text = {
                Text(text = "Sort by Timestamp")
            }
        )
        DropdownMenuItem(
            onClick = {
                val newOrder = if (currentSortOrder == NoteRepository.SortOrder.ASC) {
                    NoteRepository.SortOrder.DESC
                } else {
                    NoteRepository.SortOrder.ASC
                }
                onSortSelected(currentSortBy, newOrder)
                onDismissRequest()
            },
            text = {
                Text(
                    text = if (currentSortOrder == NoteRepository.SortOrder.ASC) "Sort Descending" else "Sort Ascending"
                )
            }
        )
    }
}


@Composable
fun Floatingactionbutton(){
    val ctx = LocalContext.current
    Box( modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 75.dp), // Add padding to keep FAB above navigation bar
        contentAlignment = Alignment.BottomEnd ){
            ExtendedFloatingActionButton(
                text = { Text(text = "New Note", color = Color.Black) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Create a new Note",
                        tint = Color.Black
                    )
                },
                onClick = {
                    val intent = Intent(ctx, Noteadd::class.java)
                    ctx.startActivity(intent)
                },
                modifier = Modifier.padding(16.dp)
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenSearchDialog(
    viewModel: NotesViewModel,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Dialog(onDismissRequest = onDismissRequest) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search Notes") },
                    navigationIcon = {
                        IconButton(onClick = onDismissRequest) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Added padding
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchNotes(it)
                    },
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth()
                )

                LazyColumn {
                    items(searchResults) { note ->
                        NoteCard(note = note, viewModel = viewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun NoteCard(note: Note, viewModel: NotesViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val ctx = LocalContext.current
    Log.d("Notecard", "Displaying note: $note")
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .background(color = Color.Transparent)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true }
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
            val formattedTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(note.timestamp), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a", Locale.getDefault()))
            Text(
                text = formattedTime,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val shareIntent = viewModel.shareNote(note)
                    ctx.startActivity(Intent.createChooser(shareIntent, "Share Note"))
                }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                }
                Spacer(modifier = Modifier.width(5.dp))
                DeleteButton(note = note, viewModel = viewModel)
            }
        }
    }
    if (showDialog) {
        EditNoteDialog(
            note = note,
            onDismissRequest = { showDialog = false },
            onConfirm = { updatedTitle, updatedBody ->
                coroutineScope.launch {
                    viewModel.updateNote(
                        note.copy(
                            title = updatedTitle,
                            body = updatedBody
                        )
                    )
                    showDialog = false
                }
            }
        )
    }
}
@Composable
fun EditNoteDialog(note: Note, onDismissRequest: () -> Unit, onConfirm: (String, String) -> Unit) {
    val title = remember { mutableStateOf(note.title) }
    val body = remember { mutableStateOf(note.body) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Edit Note", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = body.value,
                    onValueChange = { body.value = it },
                    label = { Text("Body") }
                )
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    onConfirm(title.value, body.value)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            FilledTonalButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeleteButton(note: Note, viewModel: NotesViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            showDialog = true
        },
        modifier = Modifier.size(20.dp, 20.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.Red
        )
    }

    if (showDialog) {
        DeleteConfirmationDialog(
            showDialog = showDialog,
            onConfirm = {
                coroutineScope.launch {
                    viewModel.deleteNote(note)
                    showDialog = false
                }
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}


@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Delete Note")
            },
            text = {
                Text("Are you sure you want to delete this note?")
            },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                FilledTonalButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NotesList(viewModel: NotesViewModel, selectedCategory: String) {
    val sortedNotes by viewModel.sortedNotes.collectAsState()

    val filteredNotes = if (selectedCategory == "All") {
        sortedNotes
    } else {
        sortedNotes.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 82.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(filteredNotes) { note ->
            NoteCard(note = note, viewModel = viewModel)
        }
    }
}

