package com.example.note

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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

    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("All", "Archived", "Overdue", "No Date")

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
                        color = Color.Black,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = "Sort")
                    }
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
                    contentColor = MaterialTheme.colorScheme.primary) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title,
                                fontSize = 16.sp,
                                color = if (selectedTabIndex == index) Color.Black else Color.Gray,
                                fontStyle = FontStyle.Italic,
                                fontFamily = FontFamily.SansSerif)
                            }
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 72.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        when (selectedTabIndex) {
                            0 -> { // All
                                items(allTodoItems) { todoItem ->
                                    TodoItemCard(todoItem, viewModel)
                                }
                            }
                            1 -> { // Completed
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
                    FloatingActionButton()
                }
            }
        }
    )
}

@Composable
fun FloatingActionButton() {
    val ctx = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 75.dp), // Add padding to keep FAB above navigation bar
        contentAlignment = Alignment.BottomEnd
    ) {
        ExtendedFloatingActionButton(
            text = { Text(text = "New Task", color = Color.Black) },
            icon = { Icon(imageVector = Icons.Default.NoteAdd, contentDescription = "Create a new Task", tint = Color.Black) },
            onClick = { val intent = Intent(ctx, Newlist::class.java)
                ctx.startActivity(intent)
            },
            containerColor = Color(0xFFCCC2DC),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun TodoItemCard(todoItem: TodoItem, viewModel: TodoListViewModel) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .background(color = Color.Transparent)
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { }
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
                    colors = RadioButtonDefaults.colors(unselectedColor = Color.Black, selectedColor = Color(0xFFCCC2DC))
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = todoItem.title,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = todoItem.description,
                        fontStyle = FontStyle.Normal,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    todoItem.alertTime?.format(formatter)?.let { Text(text = it,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray) }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
/**
@Composable
fun AddTodoItemDialog(viewModel: TodoListViewModel, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var alertTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var ringtoneUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val datePickerDialog = createDatePickerDialog(context) { date ->
        alertTime = alertTime?.withDate(date)?: date.atStartOfDay()
    }

    val timePickerDialog = createTimePickerDialog(context) { time ->
        alertTime = alertTime?.withTime(time)?: LocalDate.now().atTime(time)
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Add New Task") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Title") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Description") }
                )
                Row {
                    Button(onClick = { datePickerDialog.show() }) {
                        Text(text = "Pick Date")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { timePickerDialog.show() }) {
                        Text(text = "Pick Time")
                    }
                }
                RingtonePicker(onRingtoneSelected = { uri -> ringtoneUri = uri })
            }
        },
        confirmButton = {
            FilledTonalButton(onClick = {
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    viewModel.insert(TodoItem(
                        id = 0,
                        title = title,
                        description = description,
                        alertTime = alertTime,
                        ringtone = ringtoneUri?.toString()?: "",
                        completed = false
                    ))
                    scheduleNotification(context, TodoItem(
                        id = 0,
                        title = title,
                        description = description,
                        alertTime = alertTime,
                        ringtone = ringtoneUri?.toString()?: "",
                        completed = false
                    ))
                    onDismiss()
                } else {
                    Toast.makeText(context, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            FilledTonalButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
fun RingtonePicker(onRingtoneSelected: (Uri) -> Unit) {
    val context = LocalContext.current
    val ringtoneLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            uri?.let { onRingtoneSelected(it) }
        }
    }

    FilledTonalButton(onClick = {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone")
        ringtoneLauncher.launch(intent)
    }) {
        Text("Pick Ringtone")
    }
}**/

// DatePicker and TimePicker Functions
fun createDatePickerDialog(context: Context, onDateSelected: (LocalDate) -> Unit): DatePickerDialog {
    val today = LocalDate.now()
    return DatePickerDialog(context, { _, year, month, dayOfMonth ->
        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        onDateSelected(selectedDate)
    }, today.year, today.monthValue - 1, today.dayOfMonth)
}

fun createTimePickerDialog(context: Context, onTimeSelected: (LocalTime) -> Unit): TimePickerDialog {
    val now = LocalTime.now()
    return TimePickerDialog(context, { _, hourOfDay, minute ->
        val selectedTime = LocalTime.of(hourOfDay, minute)
        onTimeSelected(selectedTime)
    }, now.hour, now.minute, true)
}

fun LocalDateTime.withDate(date: LocalDate): LocalDateTime = this.withYear(date.year).withMonth(date.monthValue).withDayOfMonth(date.dayOfMonth)
fun LocalDateTime.withTime(time: LocalTime): LocalDateTime = this.withHour(time.hour).withMinute(time.minute)

fun scheduleNotification(context: Context, todoItem: TodoItem) {
    val delay = Duration.between(LocalDateTime.now(), todoItem.alertTime)
    if (delay.isNegative) {
        return // Do not schedule if the alert time is in the past
    }

    val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInitialDelay(delay)
        .setInputData(
            Data.Builder()
                .putString("title", todoItem.title)
                .putString("description", todoItem.description)
                .putString("ringtone", todoItem.ringtone)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueue(notificationWork)
}


