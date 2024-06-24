package com.example.note

import android.app.Activity
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TodoList : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        setContent {
            Main_Screen()
        }
    }
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "todo_channel",
            "To-Do Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for to-do notifications"
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@Composable
fun Main_Screen() {
    val viewModel: TodoListViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Todolist(viewModel = viewModel)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Todolist(viewModel: TodoListViewModel) {
    val allTodoItems by viewModel.allTodoItems.observeAsState(emptyList())
    val achievedTodoItems by viewModel.achievedTodoItems.observeAsState(emptyList())
    val overdueTodoItems by viewModel.overdueTodoItems.observeAsState(emptyList())
    val noDateTodoItems by viewModel.noDateTodoItems.observeAsState(emptyList())

    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        darkIcons = true
    )
    systemUiController.setNavigationBarColor(
        color = Color.Transparent,
        darkIcons = true
    )

    var showSearchDialog by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("All", "Achieved", "Overdue", "No Date")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "To-Do List",
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
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
                        onSortSelected = { sortBy, sortOrder ->
                            viewModel.sortTodoItems(sortBy, sortOrder)
                            showSortMenu = false
                        }
                    )
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    title,
                                    fontSize = 16.sp,
                                    color = if (selectedTabIndex == index) Color.Black else Color.Gray,
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 72.dp)
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        when (selectedTabIndex) {
                            0 -> { // All
                                items(allTodoItems) { todoItem ->
                                    TodoItemCard(todoItem, viewModel)
                                }
                            }
                            1 -> { // Achieved
                                items(achievedTodoItems) { todoItem ->
                                    TodoItemCard(todoItem, viewModel)
                                }
                            }
                            2 -> { // Overdue
                                items(overdueTodoItems) { todoItem ->
                                    TodoItemCard(todoItem, viewModel)
                                }
                            }
                            3 -> { // No Date
                                items(noDateTodoItems) { todoItem ->
                                    TodoItemCard(todoItem, viewModel)
                                }
                            }
                        }
                    }
                    FloatingActionbutton()
                }
            }
        }
    )

    if (showSearchDialog) {
        ShowSearchDialog(viewModel = viewModel, onDismissRequest = { showSearchDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSearchDialog(
    viewModel: TodoListViewModel,
    onDismissRequest: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.allTodoItems.observeAsState(emptyList())

    Dialog(onDismissRequest = onDismissRequest) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Search to-do list") },
                    navigationIcon = {
                        IconButton(onClick = { onDismissRequest }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            paddingValues
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                TextField(
                    value = query,
                    onValueChange = {
                        query = it
                        viewModel.searchTodoItems(query)
                    },
                    label = { Text("Search query") },
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn {
                    items(searchResults) { todoItem ->
                        TodoItemCard(todoItem = todoItem, viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun SortDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSortSelected: (String, TodoItemRepository.SortOrder) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(onClick =
        {
            onSortSelected("title", TodoItemRepository.SortOrder.ASC)
            onDismissRequest()
        },
            text = {Text("Sort by Title (A-Z)")}

        )
        DropdownMenuItem(onClick = {
            onSortSelected("title", TodoItemRepository.SortOrder.DESC)
            onDismissRequest()
        },
                text = {Text("Sort by Title (Z-A)")}

            )
        DropdownMenuItem(onClick = {
            onSortSelected("date", TodoItemRepository.SortOrder.ASC)
            onDismissRequest()
        },
            text = {Text("Sort by Date (Ascending)")}

        )
        DropdownMenuItem(onClick = {
            onSortSelected("date", TodoItemRepository.SortOrder.DESC)
            onDismissRequest()
        },
            text = { Text("Sort by Date (Descending)")}
            )
    }
}



@Composable
fun FloatingActionbutton() {
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
                val intent = Intent(ctx, Newlist::class.java)
                ctx.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun TodoItemCard(todoItem: TodoItem, viewModel: TodoListViewModel) {
        var showDeleteDialog by remember { mutableStateOf(false) }
        var showEditDialog by remember { mutableStateOf(false) }

        if (showDeleteDialog) {
            showDeleteConfirmationDialog(
                todoItem = todoItem,
                onDeleteConfirm = {
                    viewModel.delete(todoItem)
                    showDeleteDialog = false
                },
                onDismiss = { showDeleteDialog = false }
            )
        }

        if (showEditDialog) {
            showEditTodoItemDialog(
                todoItem = todoItem,
                onConfirm = { updatedItem ->
                    viewModel.update(updatedItem)
                    showEditDialog = false
                },
                onDismiss = { showEditDialog = false }
            )
        }

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .background(color = Color.Transparent)
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { showEditDialog = true }
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = todoItem.completed,
                        onClick = { viewModel.update(todoItem.copy(completed = !todoItem.completed)) },
                        modifier = Modifier.padding(0.dp),
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = Color.Black,
                            selectedColor = Color.DarkGray
                        )
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = todoItem.title,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = todoItem.description,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                        todoItem.alertTime?.format(formatter)?.let {
                            Text(
                                text = it,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
fun showEditTodoItemDialog(
    todoItem: TodoItem,
    onConfirm: (TodoItem) -> Unit,
    onDismiss: () -> Unit) {
    val title = remember { mutableStateOf(todoItem.title) }
    val description = remember { mutableStateOf(todoItem.description) }
    val alertTime = remember { mutableStateOf(todoItem.alertTime) }
    var ringtoneUri by remember { mutableStateOf<Uri?>(Uri.parse(todoItem.ringtone)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val updatedItem = todoItem.copy(
                    title = title.value,
                    description = description.value,
                    alertTime = alertTime.value,
                    ringtone = ringtoneUri?.toString() ?: ""
                )
                onConfirm(updatedItem)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Todo Item") },
        text = {
            Column {
                TextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Title") }
                )
                TextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") }
                )
                DateTimePicker(
                    alertTime = alertTime,
                    onDateSelected = { date ->
                        alertTime.value = alertTime.value?.with(date) ?: date.atStartOfDay()
                    },
                    onTimeSelected = { time ->
                        alertTime.value =
                            alertTime.value?.with(time) ?: LocalDate.now().atTime(time)
                    }
                )
                RingtonePicker(ringtoneUri) { uri -> ringtoneUri = uri }
            }
        }
    )
}

@Composable
fun RingtonePicker(currentRingtoneUri: Uri?, onRingtoneSelected: (Uri) -> Unit) {
    val context = LocalContext.current
    val ringtoneLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri =
                result.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            uri?.let { onRingtoneSelected(it) }
        }
    }

    FilledTonalButton(onClick = {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone")
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentRingtoneUri)
        }
        ringtoneLauncher.launch(intent)
    }) {
        Text("Pick Ringtone")
    }
}

@Composable
fun DateTimePicker(
    alertTime: MutableState<LocalDateTime?>,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    val datePickerDialog = createDatePickerDialog(context, onDateSelected)
    val timePickerDialog = createTimePickerDialog(context, onTimeSelected)

    Row {
        FilledTonalButton(onClick = { datePickerDialog.show() }) {
            Text("Pick Date")
        }
        Spacer(modifier = Modifier.width(8.dp))
        FilledTonalButton(onClick = { timePickerDialog.show() }) {
            Text("Pick Time")
        }
    }
}

fun createDatePickerDialog(
    context: Context,
    onDateSelected: (LocalDate) -> Unit
): DatePickerDialog {
    val today = LocalDate.now()
    return DatePickerDialog(context, { _, year, month, dayOfMonth ->
        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        onDateSelected(selectedDate)
    }, today.year, today.monthValue - 1, today.dayOfMonth)
}

fun createTimePickerDialog(
    context: Context,
    onTimeSelected: (LocalTime) -> Unit
): TimePickerDialog {
    val now = LocalTime.now()
    return TimePickerDialog(context, { _, hourOfDay, minute ->
        val selectedTime = LocalTime.of(hourOfDay, minute)
        onTimeSelected(selectedTime)
    }, now.hour, now.minute, true)
}

@Composable
fun showDeleteConfirmationDialog(todoItem: TodoItem, onDeleteConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Delete Todo Item") },
        text = { Text("Are you sure you want to delete this todo item?") }
    )
}
        fun LocalDateTime.withDate(date: LocalDate): LocalDateTime =
            this.withYear(date.year).withMonth(date.monthValue).withDayOfMonth(date.dayOfMonth)

        fun LocalDateTime.withTime(time: LocalTime): LocalDateTime =
            this.withHour(time.hour).withMinute(time.minute)



