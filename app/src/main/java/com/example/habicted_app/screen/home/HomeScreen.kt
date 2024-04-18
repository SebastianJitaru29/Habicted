package com.example.habicted_app.screen.home

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.navigation.graphs.HomeNavGraph
import com.example.habicted_app.navigation.graphs.NavBar
import com.example.habicted_app.screen.groups.GroupAddDialog
import com.example.habicted_app.screen.taskscreen.TaskDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    var screen = listOf(NavBar.Tasks, NavBar.Groups, NavBar.Settings)
    val homeViewModel: HomeViewModel = hiltViewModel()


    Scaffold(
        bottomBar = { NavBottomBar(navController = navController) },
        floatingActionButton = {
            NavFloatingActionButton(
                navController = navController,
                onEvent = homeViewModel::onEvent
            )
        }
    ) {
        HomeNavGraph(
            navController = navController,
            modifier = Modifier.padding(it),
            homeUIState = homeViewModel.homeUiState.collectAsState().value,
            onEvent = homeViewModel::onEvent
        )
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
fun NavFloatingActionButton(
    navController: NavHostController,
    onEvent: (HomeUiEvents) -> Unit = { },
) {
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
            TaskDialog(
                onDismiss = { addButton.value = AddButton.None },
                onConfirm = { title, description, group, date ->
                    onEvent(
                        HomeUiEvents.SaveTask(
                            Task(
                                id = 0,
                                groupId = group,
                                name = title,
                                description = description,
                                date = date,
                                isDone = false,
                                streakDays = 0,
                                done = 0,
                                total = 0
                            )
                        )
                    )
                    addButton.value = AddButton.None
                }
            )

        }

        AddButton.Group -> {
            GroupAddDialog(
                onDismiss = { addButton.value = AddButton.None },
                onConfirm = { name, color, members ->
                    onEvent(
                        HomeUiEvents.SaveGroup(
                            Group(
                                id = 0,
                                name = name,
                                color = color,
                                members = members,
                                tasksList = emptyList()
                            )
                        )
                    )
                    addButton.value = AddButton.None
                })
        }

        else -> {}
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