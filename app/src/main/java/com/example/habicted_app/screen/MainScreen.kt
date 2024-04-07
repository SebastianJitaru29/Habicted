package com.example.habicted_app.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.routes.HomeDestination
import com.example.habicted_app.routes.Overview
import com.example.habicted_app.routes.allItemsNav
import com.example.habicted_app.ui.theme.HabictedAppTheme
import kotlin.coroutines.coroutineContext

@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var currentIndex: Int by rememberSaveable {
        mutableStateOf(0)
    }
    val context = LocalContext.current
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                allItemsNav.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = currentIndex == index,
                        onClick = { currentIndex = index },
                        icon = item.icon
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Check the current destination
                    when (currentDestination) { //Aixo no funcionara a no ser que cambiem tema rutes
                        "main" -> {
                            // Show "Hello" message if on screen A
                            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show()
                        }
                        "home" -> {
                            // Show "World" message if on screen B
                            Toast.makeText(context, "World", Toast.LENGTH_SHORT).show()
                        }
                        // Add more cases for other screens if needed
                        else -> {
                            // Handle other cases or do nothing
                        }
                    }
                }
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }

    ) { innerPadding ->

        Box(modifier = modifier.padding(innerPadding)) {
            allItemsNav.get(currentIndex).screen(navController)
        }
    }
}

@Composable
@Preview
fun NavBarPrev() {
    val navController = rememberNavController()
    HabictedAppTheme {
        MainScreen(navController = navController)
    }
}


