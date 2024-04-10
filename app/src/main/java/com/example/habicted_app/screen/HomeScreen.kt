package com.example.habicted_app.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.navigation.graphs.HomeNavGraph
import com.example.habicted_app.navigation.graphs.NavBar
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

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
    val addButton = rememberSaveable { mutableStateOf(AddButton.none) }
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
                onDismiss = { addButton.value = AddButton.none },
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
                    addButton.value = AddButton.none
                }
            )

        }

        AddButton.Group -> {
            addButton.value = AddButton.none
        }

        else -> {
        }
    }

}

private enum class AddButton {
    Task, Group, none
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, LocalDate) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("New Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") }
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") }
                    )
                    TaskDatePicker(onConfirm = { date = it })
                }
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm(title, description, location, date)
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(onConfirm: (LocalDate) -> Unit = {}) {
    val datePickerState = rememberDatePickerState()
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val date = datePickerState.selectedDateMillis?.let { longToLocalDate(it).toString() }

    Row {
        IconButton(onClick = { showDialog.value = true }) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "pick Date")
        }
        Text(
            text = date ?: "Pick a date",
            modifier = Modifier
                .padding(8.dp)
                .align(
                    Alignment.CenterVertically
                )
        )
    }


    if (showDialog.value) {
        DatePickerDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    if (datePickerState.selectedDateMillis != null) {
                        onConfirm(longToLocalDate(datePickerState.selectedDateMillis!!))
                        showDialog.value = false
                    }
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun longToLocalDate(value: Long): LocalDate {
    return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun NavBar() {
    HomeScreen()
}