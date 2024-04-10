package com.example.habicted_app.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.example.habicted_app.screen.groups.GroupScreen
import com.example.habicted_app.screen.groups.GroupUIState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupsRoute(parentEntry: NavBackStackEntry, groupList: List<GroupUIState>) {
//    val groupsViewModel: GroupsViewModel = hiltViewModel(parentEntry)
//    val groupList by groupsViewModel.groupList.collectAsState()

    GroupScreen(
        groupList = groupList,
    )
}