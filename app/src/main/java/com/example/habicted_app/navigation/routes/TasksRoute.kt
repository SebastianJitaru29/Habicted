package com.example.habicted_app.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habicted_app.R
import com.example.habicted_app.data.model.Task
import com.example.habicted_app.screen.home.HomeUiEvents
import com.example.habicted_app.screen.taskscreen.TaskScreen
import com.example.habicted_app.screen.taskscreen.TaskUIEvents
import com.example.habicted_app.screen.taskscreen.TaskUIState
import com.example.habicted_app.screen.taskscreen.UserViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksRoute(
    selectedDate: LocalDate,
    tasksList: List<Task>,
    onEvent: (HomeUiEvents) -> Unit,
    onProfileClick: () -> Unit,
    onTaskUIEvents: (TaskUIEvents) -> TaskUIState?,
) {
    val viewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
    val username by viewModel.userName.collectAsState()
    val profileId = viewModel.profilePicture.value ?: R.drawable.outline_groups_24

    TaskScreen(
        selectedDate = selectedDate,
        username = username,
        profilePicture = profileId,
        onProfilePic = onProfileClick,
        tasksList = tasksList,
        onEvent = onEvent,
        onTaskUIEvents = onTaskUIEvents
    )

}