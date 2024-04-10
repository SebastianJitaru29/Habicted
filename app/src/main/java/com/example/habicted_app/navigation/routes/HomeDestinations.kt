package com.example.habicted_app.navigation.routes

import SettingsScreen
import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.habicted_app.R
import com.example.habicted_app.data.repository.local.LocalGroupRepository
import com.example.habicted_app.data.repository.local.LocalUserRepository
import com.example.habicted_app.screen.groups.GroupScreen
import com.example.habicted_app.screen.groups.GroupsViewModel
import com.example.habicted_app.screen.preferences.MainViewModel
import com.example.habicted_app.screen.taskscreen.TaskScreen
import com.example.habicted_app.screen.taskscreen.UserViewModel

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
}

object Groups : HomeDestination {
    override val icon =
        @Composable {
            Icon(
                painter = painterResource(id = R.drawable.outline_groups_24),
                contentDescription = "Groups"
            )
        }
    override val route = "home"

    @RequiresApi(Build.VERSION_CODES.O)
    override val screen = @Composable { navController: NavHostController ->
//        val viewModel = GroupsViewModel(LocalGroupRepository())
//        GroupScreen(
//            groupList = viewModel.groups.value ?: emptyList(),

//            )
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
        //SettingsScreen(mainViewModel =)
    }
}


// Screens to be displayed in the top RallyTabRow
val allItemsNav = listOf(Overview, Groups, Settings)