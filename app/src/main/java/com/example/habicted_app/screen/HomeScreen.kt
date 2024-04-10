package com.example.habicted_app.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.navigation.graphs.HomeNavGraph
import com.example.habicted_app.navigation.graphs.NavBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    var screen = listOf(NavBar.Tasks, NavBar.Groups, NavBar.Settings)
    Scaffold(
        bottomBar = { NavBottomBar(navController = navController) },
        floatingActionButton = { NavFloatingActionButton(navController = navController) }


    ) {
        HomeNavGraph(navController = navController, Modifier.padding(it))
    }
}


@Composable
fun NavBottomBar(navController: NavHostController) {
    val screens = listOf(NavBar.Tasks, NavBar.Groups, NavBar.Settings)

    NavigationBar {
        screens.forEach { navBar ->
            NavigationBarItem(
                icon = { Icon(navBar.icon, contentDescription = null) },
                label = { navBar.title },
                selected = navController.currentDestination?.route == navBar.route,
                onClick = {
                    navController.navigate(navBar.route)
                }
            )
        }
    }
}

@Composable
fun NavFloatingActionButton(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        NavBar.Tasks.route -> {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(16.dp),
                elevation = FloatingActionButtonDefaults.elevation()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun NavBar() {
    //HomeScreen()
}