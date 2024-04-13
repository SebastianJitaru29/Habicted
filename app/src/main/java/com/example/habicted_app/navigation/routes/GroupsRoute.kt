package com.example.habicted_app.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.habicted_app.screen.groups.GroupScreen
import com.example.habicted_app.screen.groups.GroupUIState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupsRoute(groupList: List<GroupUIState>) {
    GroupScreen(
        groupList = groupList,
    )
}