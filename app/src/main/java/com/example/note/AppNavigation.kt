package com.example.note

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class AppNavigation : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App_Navigation()
            }
        }
    }


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App_Navigation() {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry:NavBackStackEntry? by navController.currentBackStackEntryAsState()
                val currentDestination:NavDestination? = navBackStackEntry?.destination

                listOfNavItems.forEach{ navItem : NavItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route} == true,
                        onClick = {
                            navController.navigate(navItem.route){
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector =navItem.icon,
                                contentDescription = null)
                        },
                        label = {
                            Text(text = navItem.label,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold )
                        }
                    )
                }

            }
        }
    ){paddingValues -> paddingValues
        NavHost(
            navController = navController ,
            startDestination = Screens.HomeScreen.name,
            builder = {
                composable(route = Screens.HomeScreen.name){
                    MainScreen()
                }
                composable(route = Screens.TodoList.name){
                    Main_Screen()
                }
                composable(route = Screens.Shedular.name){
                    Shedules()
                }
            })
    }
}