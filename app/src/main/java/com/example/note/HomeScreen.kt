package com.example.note

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun HomeScreen()
{
    Surface (
   modifier = Modifier
       .fillMaxSize()
       .background(color = Color.White)
){
    Box {
        Greeting()

        Row (modifier = Modifier
            .size(40.dp)
            .background(color = Color.Black)
            .offset(y = (-45).dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Absolute.Right
        ){
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add button",
                    modifier = Modifier.size(35.dp))
            }
        }

    }
}
}

@Composable
fun Greeting(){
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = AbsoluteAlignment.Right
        ) {
           IconButton(onClick = { /*TODO*/ }) {
              Icon(imageVector = Icons.Default.FilterList,
                  contentDescription = "Filter Icon")
           }
            Spacer(modifier = Modifier.width(15.dp))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon")

            }
        }
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when {
            currentHour in 0..11 -> "Good Morning"
            currentHour in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }

        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
        val dateString = dateFormat.format(java.util.Date())

        Column {
            Text(
                text = greeting,
                fontSize = 18.sp,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = dateString,
                fontSize = 18.sp,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "User's Name",
                fontSize = 18.sp,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        }

    }
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        modifier = Modifier
            .background(color = Color.Black)
            .background(
                color = colors.background,
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxSize()
    ) {
        Column {
            Text(
                text = "Notepad",
                fontSize = 24.sp,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif)
        }
    }
}

@Composable
fun Search(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Add the search icon
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search",
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp)
        )

        // Add the text field
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Search notes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
}