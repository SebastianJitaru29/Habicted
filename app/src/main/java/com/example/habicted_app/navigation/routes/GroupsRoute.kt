package com.example.habicted_app.navigation.routes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.habicted_app.data.model.Group
import com.example.habicted_app.screen.groups.GroupScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GroupsRoute(groupList: List<Group>) {
    GroupScreen(
        groupList = groupList
    )
}