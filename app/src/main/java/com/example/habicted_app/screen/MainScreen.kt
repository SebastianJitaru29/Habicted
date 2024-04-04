package com.example.habicted_app.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.habicted_app.routes.HomeDestination
import com.example.habicted_app.routes.Overview
import com.example.habicted_app.routes.allItemsNav
import com.example.habicted_app.ui.theme.HabictedAppTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var currentIndex: Int by rememberSaveable {
        mutableStateOf(0)
    }


    Scaffold(
        bottomBar = {
            NavigationBar {
                allItemsNav.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = currentIndex == index,
                        onClick = { currentIndex = index },
                        icon = item.icon
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { innerPadding ->

        Box(modifier = modifier.padding(innerPadding)) {
            allItemsNav.get(currentIndex).screen()
        }
    }
}


@Preview
@Composable
fun NavBarPrev() {
    HabictedAppTheme {
        MainScreen()
    }
}

