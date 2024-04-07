package com.example.habicted_app.routes

import SettingsScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.habicted_app.Greeting
import com.example.habicted_app.R
import com.example.habicted_app.screen.GroupScreen
import com.example.habicted_app.screen.taskscreen.TaskScreen

interface HomeDestination {
    val icon: @Composable () -> Unit
    val route: String
    val screen: @Composable (NavHostController) -> Unit
}


object Overview : HomeDestination {
    override val icon =
        @Composable {
            Icon(
                painter = painterResource(id = R.drawable.outline_checklist_24),
                contentDescription = "Home"
            )
        }
    override val route = "home"
    @RequiresApi(Build.VERSION_CODES.O)
    override val screen = @Composable { navController: NavHostController ->
        TaskScreen(navController = navController)
    }
}

object Groups : HomeDestination {
    override val icon =
        @Composable {
            Icon(
                painter = painterResource(id = R.drawable.outline_groups_24),
                contentDescription = "Groups"
            )
        }
    override val route = "groups"
    override val screen = @Composable { navController: NavHostController ->
        // Replace GroupScreen with your actual implementation
        GroupScreen()
    }
}

object Settings : HomeDestination {
    override val icon =
        @Composable {
            Icon(
                painter = painterResource(id = R.drawable.outline_settings_24),
                contentDescription = "Settings"
            )
        }
    override val route = "settings"
    override val screen = @Composable { navController: NavHostController ->
        // Replace Greeting with your actual settings screen implementation
        SettingsScreen()
    }
}


// Screens to be displayed in the top RallyTabRow
val allItemsNav = listOf(Overview, Groups, Settings)