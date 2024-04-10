package com.example.habicted_app.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.habicted_app.screen.groups.GroupScreen
import com.example.habicted_app.screen.groups.GroupsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupsRoute(parentEntry: NavBackStackEntry) {
    val groupsViewModel: GroupsViewModel = hiltViewModel(parentEntry)
    val groupList by groupsViewModel.groupList.collectAsState()

    GroupScreen(groupList = groupList, addGroup = groupsViewModel::addGroup)
}