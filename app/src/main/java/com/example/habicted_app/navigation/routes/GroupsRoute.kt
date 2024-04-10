package com.example.habicted_app.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import com.example.habicted_app.screen.groups.GroupScreen
import com.example.habicted_app.screen.groups.GroupsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupsRoute() {
    val viewModel: GroupsViewModel = viewModel(factory = GroupsViewModel.Factory)
    val groupList by viewModel.groupList.collectAsState()

    GroupScreen(groupList = groupList, addGroup = viewModel::addGroup)
}