package com.example.habicted_app.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.data.model.Task
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavFloatingActionButton(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val addButton = rememberSaveable { mutableStateOf(AddButton.None) }
    when (navBackStackEntry?.destination?.route) {
        NavBar.Tasks.route -> {
            FloatingActionButton(
                onClick = { addButton.value = AddButton.Task },
                modifier = Modifier.padding(16.dp),
                elevation = FloatingActionButtonDefaults.elevation()
            ) {
                Icon(Icons.Default.AddTask, contentDescription = "Add task")
            }
        }

        NavBar.Groups.route -> {
            FloatingActionButton(
                onClick = { addButton.value = AddButton.Group },
                modifier = Modifier.padding(16.dp),
                elevation = FloatingActionButtonDefaults.elevation()
            ) {
                Icon(Icons.Default.GroupAdd, contentDescription = "Add group")
            }

        }
    }

    when (addButton.value) {
        AddButton.Task -> {
            var newTask by remember { mutableStateOf(Task()) }
            TaskDialog(
                showDialog = true,
                onDismiss = { addButton.value = AddButton.None },
                onConfirm = { title, description, location, date ->
                    newTask = Task(
                        id = 0,
                        groupId = 0,
                        name = title,
                        description = description,
                        date = date,
                        isDone = false,
                        streakDays = 0,
                        done = 0,
                        total = 0
                    )
                    addButton.value = AddButton.None
                }
            )

        }

        AddButton.Group -> {
            addButton.value = AddButton.None
        }

        else -> {
        }
    }

}

private enum class AddButton {
    Task, Group, None
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun NavBar() {
    HomeScreen()
}