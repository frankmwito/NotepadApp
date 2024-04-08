package com.example.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDefaults.backgroundColor
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun MainScreen() {
    val windowSize = rememberWindowSize()
    when(windowSize.width){
        WindowType.Compact -> {
            CompactMainScreen()
        } else -> {
            MediumToExpandedMainScreen()
        }
    }
}
@Composable
fun MediumToExpandedMainScreen(){
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomeScreen()

        }
    }
}

@Composable
fun CompactMainScreen(){
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HomeScreen()

        }
    }
}
@Composable
fun HomeScreen() {
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
                Spacer(modifier = Modifier.width(135.dp))
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
                    .offset(y = 650.dp) // adjust the value to move the FAB up
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                FloatingActionButton(
                    onClick = { /* Do something when the FAB is clicked */ },
                    containerColor = Color.Black,
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add",
                        tint = Color.White)
                }
            }
        }
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
@Composable
fun Notelist(){


}

