package com.example.habicted_app.navigation.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habicted_app.navigation.routes.GroupsRoute
import com.example.habicted_app.navigation.routes.SettingsRoute
import com.example.habicted_app.navigation.routes.TasksRoute
import com.example.habicted_app.screen.preferences.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        route = Graphs.HOME,
        startDestination = NavBar.Tasks.route,
        modifier = modifier
    ) {
        composable(route = NavBar.Tasks.route) {
            TasksRoute()
        }
        composable(route = NavBar.Groups.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graphs.HOME)
            }
            GroupsRoute(parentEntry)
        }
        composable(route = NavBar.Settings.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graphs.HOME)
            }
            val mainViewModel = hiltViewModel<MainViewModel>(parentEntry)
            SettingsRoute(mainViewModel)
        }
    }
}


sealed class NavBar(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    data object Tasks : NavBar(
        route = "tasks",
        title = "Tasks",
        icon = Icons.Default.Checklist
    )

    data object Groups : NavBar(
        route = "groups",
        title = "Groups",
        icon = Icons.Default.Groups
    )

    data object Settings : NavBar(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}