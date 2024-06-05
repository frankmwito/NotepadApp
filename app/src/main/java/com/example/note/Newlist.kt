package com.example.note

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.LocalDate
import java.time.LocalDateTime

class Newlist : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

           newList( viewModel = viewModel(),
               onBackPressed = { onBackPressed()}
           )
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun newList(viewModel: TodoListViewModel, onBackPressed: () -> Unit) {
    val title by remember { mutableStateOf("") }
    val description by remember { mutableStateOf("") }
    val alertTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var ringtoneUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
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
                    Text(text = "New Task",
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(8.dp)
                        )
                },
                navigationIcon = {
                                 IconButton(onClick = { onBackPressed }) {
                                     Icon(imageVector = Icons.Default.ArrowBack ,
                                         contentDescription = ("Back"))

                                 }
                },
                actions = {
                    IconButton(onClick = {  if (title.isNotEmpty() && description.isNotEmpty()) {
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
                       //onDismiss()
                    } else {
                        Toast.makeText(context, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show()
                    } }) {
                        Icon(imageVector = Icons.Filled.SaveAlt,
                            contentDescription = "Save" )

                    }
                }
            )

            },
        content = {innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
            ){
                DateTimeRingtone(onRingtoneSelected = { uri -> ringtoneUri = uri })
                Textfields()
            }
        }
    )
}

@Composable
fun DateTimeRingtone(onRingtoneSelected: (Uri) -> Unit){
    var alertTime by remember { mutableStateOf<LocalDateTime?>(null) }
    val context = LocalContext.current
    val datePickerDialog = createDatePickerDialog(context) { date ->
        alertTime = alertTime?.withDate(date)?: date.atStartOfDay()
    }

    val timePickerDialog = createTimePickerDialog(context) { time ->
        alertTime = alertTime?.withTime(time)?: LocalDate.now().atTime(time)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilledTonalButton(onClick = { datePickerDialog.show() })
        {
            Text(text = "Pick Date",
                color = Color.Black)
        }
        Spacer(modifier = Modifier.width(5.dp))
        FilledTonalButton(onClick = {timePickerDialog.show() })
        {
            Text(text = "Pick Time",
                color = Color.Black)
        }
        Spacer(modifier = Modifier.width(5.dp))
        val ringtoneLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                uri?.let { onRingtoneSelected(it) }
            }
        }
        FilledTonalButton(
            onClick =
            { val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone")
            ringtoneLauncher.launch(intent) })
        {
            Text(text = "Ringtone",
                color = Color.Black)
        }
    }
}

@Composable
fun Textfields() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .offset(y = 70.dp)
    ) {
        TextField(
            value = title,
            onValueChange =  {title = it},
            label = {
                androidx.compose.material3.Text(
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
        TextField(
            value = description,
            onValueChange = {description = it},
            label = {
                Text(
                    text = "description",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.SansSerif)
            },
            modifier = Modifier.fillMaxWidth(),
            )
    }

}