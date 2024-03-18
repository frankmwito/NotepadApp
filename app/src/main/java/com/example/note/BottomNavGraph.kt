package com.example.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.note.ui.theme.NoteTheme

@Composable
fun BottomNavGraph(navController:NavHostController, contentPadding: PaddingValues){
NavHost(
    navController = navController ,
    startDestination = BottomBarScreen.HOME.route,
    builder = {
        composable(route = BottomBarScreen.HOME.route){
            HomeScreen()
        }
        composable(route = BottomBarScreen.TODOLIST.route){
            Todolist()
        }
        composable(route = BottomBarScreen.SHEDULES.route){
            shedular()
        }
    } )
}
