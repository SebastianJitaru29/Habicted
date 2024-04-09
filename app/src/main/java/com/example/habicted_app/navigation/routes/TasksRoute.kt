package com.example.habicted_app.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.habicted_app.R
import com.example.habicted_app.data.repository.local.LocalUserRepository
import com.example.habicted_app.screen.taskscreen.TaskScreen
import com.example.habicted_app.screen.taskscreen.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksRoute() {
    val navController = rememberNavController()
    val viewModel = UserViewModel(LocalUserRepository())
    val username = viewModel.userName.value ?: "User"
    val profileId = viewModel.profilePicture.value ?: R.drawable.outline_groups_24
    TaskScreen(
        navController = navController,
        username = username,
        profilePicture = profileId,
        tasksList = emptyList()
    )

}