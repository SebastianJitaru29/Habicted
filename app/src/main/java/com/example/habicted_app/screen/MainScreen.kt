package com.example.habicted_app.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.habicted_app.routes.HomeDestination
import com.example.habicted_app.routes.Overview
import com.example.habicted_app.routes.allItemsNav

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var currentScreen: HomeDestination by rememberSaveable { mutableStateOf(Overview) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                allItemsNav.forEach { item ->
                    NavigationBarItem(
                        selected = currentScreen == item,
                        onClick = { currentScreen = item },
                        icon = { Icon(imageVector = item.icon, contentDescription = "") })
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            currentScreen.screen()
        }
    }
}

