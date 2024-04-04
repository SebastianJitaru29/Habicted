package com.example.habicted_app.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.habicted_app.Greeting
import com.example.habicted_app.R
import com.example.habicted_app.screen.GroupScreen

interface HomeDestination {
    val icon: @Composable () -> Unit
    val route: String
    val screen: @Composable () -> Unit
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
    override val screen = @Composable { Greeting(name = "HOME") }
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
    override val screen = @Composable { GroupScreen()}
}

object Settings : HomeDestination {
    override val icon =
        @Composable {
            Icon(
                painter = painterResource(id = R.drawable.outline_settings_24),
                contentDescription = "Home"
            )
        }
    override val route = "home"
    override val screen = @Composable { Greeting(name = "Settings") }
}

// Screens to be displayed in the top RallyTabRow
val allItemsNav = listOf(Overview, Groups, Settings)