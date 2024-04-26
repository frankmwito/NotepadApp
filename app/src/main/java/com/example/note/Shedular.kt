package com.example.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class Shedular : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           Shedules()
        }
    }
}

@Composable
fun Shedules(){
   Surface (
       modifier = Modifier
           .fillMaxSize()
           .background(color = Color.White)
   ){
      Box {
          Row (
              modifier = Modifier.fillMaxSize(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
          ){
              Card (
                  modifier = Modifier
                      .fillMaxSize()
                      .background(color = Color.Gray)
              ){
                  Text(
                      text = "Welcome to ShedularScreen",
                      color = Color.Black,
                      fontStyle = FontStyle.Italic,
                      fontSize = 24.sp,
                      fontWeight = FontWeight.Bold,
                      fontFamily = FontFamily.SansSerif
                  )
              }
          }

      }
   }
}