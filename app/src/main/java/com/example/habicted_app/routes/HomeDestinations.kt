package com.example.habicted_app.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.habicted_app.Greeting

interface HomeDestination {
    val icon: ImageVector
    val route: String
    val screen: @Composable () -> Unit
}


object Overview : HomeDestination {
    override val icon = Icons.Filled.Home
    override val route = "home"
    override val screen: @Composable () -> Unit = { Greeting(name = "HOME") }
}

object Groups : HomeDestination {
    override val icon = Icons.Filled.Person
    override val route = "groups"
    override val screen: @Composable () -> Unit = { Greeting(name = "Groups") }
}

object Settings : HomeDestination {
    override val icon = Icons.Filled.Settings
    override val route = "settings"
    override val screen: @Composable () -> Unit = { Greeting(name = "Settings") }
}

// Screens to be displayed in the top RallyTabRow
val allItemsNav = listOf(Overview, Groups, Settings)