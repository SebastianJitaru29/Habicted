package com.example.habicted_app.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.R
import com.example.habicted_app.data.repository.local.LocalUserRepository
import com.example.habicted_app.screen.taskscreen.TaskScreen
import com.example.habicted_app.screen.taskscreen.TasksViewModel
import com.example.habicted_app.screen.taskscreen.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksRoute() {
    val navController = rememberNavController()

    val viewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
    val username by viewModel.userName.collectAsState()
    val profileId = viewModel.profilePicture.value ?: R.drawable.outline_groups_24

    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModel.Factory)
    val tasksList by tasksViewModel.displayTasks.collectAsState()
    TaskScreen(
        navController = navController,
        username = username,
        profilePicture = profileId,
        tasksList = tasksList,
    )

}